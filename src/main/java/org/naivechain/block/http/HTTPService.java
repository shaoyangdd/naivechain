package org.naivechain.block.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.naivechain.block.http.servlet.AddPeerServlet;
import org.naivechain.block.http.servlet.BlocksServlet;
import org.naivechain.block.http.servlet.MineBlockServlet;
import org.naivechain.block.http.servlet.PeersServlet;
import org.naivechain.block.mine.BlockService;
import org.naivechain.block.p2p.P2PService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP服务类，作为服务端对外提供接口
 *
 * @author kangshaofei
 * @date 2019-08-18
 */
public class HTTPService {

    private static final Logger logger = LoggerFactory.getLogger(HTTPService.class);

    /**
     * 区块服务类
     */
    private BlockService blockService;

    /**
     * 点对点通讯服务类
     */
    private P2PService p2pService;

    public HTTPService(BlockService blockService, P2PService p2pService) {
        this.blockService = blockService;
        this.p2pService = p2pService;
    }

    /**
     * 初始化HTTP服务器
     *
     * @param port 端口号
     */
    public void initHTTPServer(int port) {
        logger.info("初始化HTTP服务器,监听端口号:{}", port);
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new BlocksServlet()), "/blocks");
        context.addServlet(new ServletHolder(new MineBlockServlet()), "/mineBlock");
        context.addServlet(new ServletHolder(new PeersServlet()), "/peers");
        context.addServlet(new ServletHolder(new AddPeerServlet()), "/addPeer");
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            logger.error("初始化HTTP服务器失败:{}", e);
        }
        logger.info("初始化HTTP服务器成功");
    }
}

