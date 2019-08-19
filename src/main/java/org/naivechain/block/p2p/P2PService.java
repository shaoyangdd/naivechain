package org.naivechain.block.p2p;

import com.alibaba.fastjson.JSON;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.server.WebSocketServer;
import org.naivechain.block.common.enums.MessageType;
import org.naivechain.block.mine.Block;
import org.naivechain.block.mine.BlockService;
import org.naivechain.block.transaction.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * P2P服务类
 *
 * @author kangshaofei
 * @date 2019-08-19
 */
public class P2PService {

    private static final Logger logger = LoggerFactory.getLogger(P2PService.class);

    /**
     * 连接到本节点的其它节点
     */
    private List<WebSocket> sockets;

    /**
     * 区块服务类
     */
    private BlockService blockService;

    public P2PService(BlockService blockService) {
        this.blockService = blockService;
        this.sockets = new ArrayList<WebSocket>();
    }

    /**
     * 初始化P2P服务
     *
     * @param port 端口号，监听此端口
     */
    public void initP2PServer(int port) {
        logger.info("初始化P2P服务");
        final WebSocketServer socket = new WebSocketServer(new InetSocketAddress(port)) {
            public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
                logger.info("P2P服务打开连接，IP:{}", webSocket.getRemoteSocketAddress());
                write(webSocket, queryChainLengthMsg());
                sockets.add(webSocket);
            }

            public void onClose(WebSocket webSocket, int i, String s, boolean b) {
                logger.info("P2P服务关闭连接，IP:{}:", webSocket.getRemoteSocketAddress());
                sockets.remove(webSocket);
            }

            public void onMessage(WebSocket webSocket, String s) {
                logger.info("P2P服务收到消息，IP:{}", webSocket.getRemoteSocketAddress());
                handleMessage(webSocket, s);
            }

            public void onError(WebSocket webSocket, Exception e) {
                logger.info("P2P服务异常: IP:{}", webSocket.getRemoteSocketAddress());
                logger.error("异常信息:{}", e);
                sockets.remove(webSocket);
            }

            public void onStart() {
                logger.info("P2P服务启动");
            }
        };
        socket.start();
        logger.info("初始化P2P服务成功，监听端口号为:{}", port);
    }

    /**
     * 处理消息
     *
     * @param webSocket     websocket
     * @param messageString 消息
     */
    private void handleMessage(WebSocket webSocket, String messageString) {
        Message message = JSON.parseObject(messageString, Message.class);
        logger.info("处理消息:{}", messageString);
        if (MessageType.QUERY_LAST_BLOCK_CHAIN.getCode().equals(message.getType())) {
            write(webSocket, responseLatestMsg());
        } else if (MessageType.QUERY_ALL_BLOCK_CHAIN.getCode().equals(message.getType())) {
            write(webSocket, responseChainMsg());
        } else if (MessageType.RESPONSE_BLOCK_CHAIN.getCode().equals(message.getType())) {
            handleBlockChainResponse(message.getData());
        }
    }

    /**
     * 处理区块
     * @param message 打包的区块
     */
    private void handleBlockChainResponse(String message) {
        List<Block> receiveBlocks = JSON.parseArray(message, Block.class);
        Collections.sort(receiveBlocks, new Comparator<Block>() {
            public int compare(Block o1, Block o2) {
                return o1.getIndex() - o2.getIndex();
            }
        });

        Block latestBlockReceived = receiveBlocks.get(receiveBlocks.size() - 1);
        Block latestBlock = blockService.getLatestBlock();
        if (latestBlockReceived.getIndex() > latestBlock.getIndex()) {
            if (latestBlock.getHash().equals(latestBlockReceived.getPreviousHash())) {
                logger.info("添加新的区块到链上");
                blockService.addBlock(latestBlockReceived);
                broadcast(responseLatestMsg());
            } else if (receiveBlocks.size() == 1) {
                logger.info("We have to query the chain from our peer");
                broadcast(queryAllMsg());
            } else {
                blockService.replaceChain(receiveBlocks);
            }
        } else {
            logger.info("接收到的区块没有原来的长，啥也不做了");
        }
    }

    /**
     * 连接到其它节点
     * @param peer 其它节点的IP端口
     */
    public void connectToPeer(String peer) {
        logger.info("连接到其它节点{}...", peer);
        try {
            final WebSocketClient socket = new WebSocketClient(new URI(peer)) {

                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    logger.info("connectToPeer onOpen");
                    write(this, queryChainLengthMsg());
                    sockets.add(this);
                }

                @Override
                public void onMessage(String s) {
                    logger.info("connectToPeer onMessage");
                    handleMessage(this, s);
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    logger.info("connectToPeer onClose");
                    sockets.remove(this);
                }

                @Override
                public void onError(Exception e) {
                    logger.info("connectToPeer onError");
                    sockets.remove(this);
                }
            };
            socket.connect();
            logger.info("连接到其它节点成功");
        } catch (URISyntaxException e) {
            logger.error("p2p 连接到其它节点失败：{}" + e.getMessage());
        }
    }

    /**
     * 发送消息
     * @param ws WebSocket
     * @param message 消息
     */
    private void write(WebSocket ws, String message) {
        ws.send(message);
    }

    /**
     * 广播消息到其它节点
     *
     * @param message 消息
     */
    private void broadcast(String message) {
        for (WebSocket socket : sockets) {
            this.write(socket, message);
        }
    }

    private String queryAllMsg() {
        return JSON.toJSONString(new Message(MessageType.QUERY_ALL_BLOCK_CHAIN.getCode()));
    }

    private String queryChainLengthMsg() {
        return JSON.toJSONString(new Message(MessageType.QUERY_LAST_BLOCK_CHAIN.getCode()));
    }

    private String responseChainMsg() {
        String blockChain = JSON.toJSONString(blockService.getBlockChain());
        return JSON.toJSONString(new Message(MessageType.RESPONSE_BLOCK_CHAIN.getCode(), blockChain));
    }

    public String responseLatestMsg() {
        Block[] blocks = {blockService.getLatestBlock()};
        return JSON.toJSONString(new Message(MessageType.RESPONSE_BLOCK_CHAIN.getCode(), JSON.toJSONString(blocks)));
    }

    public List<WebSocket> getSockets() {
        return sockets;
    }
}
