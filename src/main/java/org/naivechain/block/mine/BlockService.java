package org.naivechain.block.mine;

import org.naivechain.block.crypto.CryptoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 区块服务类
 *
 * @author kangshaofei
 * @date 2019-08-19
 */
public class BlockService {

    private static final Logger logger = LoggerFactory.getLogger(BlockService.class);

    /**
     * 所有区块
     */
    private List<Block> blockChain;

    public BlockService() {
        this.blockChain = new ArrayList<Block>();
        blockChain.add(this.getFirstBlock());
    }

    /**
     * 获取SHA256
     *
     * @param index        索引
     * @param previousHash 上一HASH
     * @param timestamp    时间戳
     * @param data         交易数据
     * @return SHA256
     */
    private String calculateHash(int index, String previousHash, long timestamp, String data) {
        return CryptoUtil.getSHA256(previousHash + timestamp + data);
    }

    /**
     * 获取最后的区块
     * @return Block
     */
    public Block getLatestBlock() {
        return blockChain.get(blockChain.size() - 1);
    }

    /**
     * 获取第一个区块
     *
     * @return Block
     */
    private Block getFirstBlock() {
        return new Block(1, "0", System.currentTimeMillis(), "Hello Block", "aa212344fc10ea0a2cb885078fa9bc2354e55efc81be8f56b66e4a837157662e");
    }

    /**
     * 生成下一个区块
     * @param blockData 交易打包的区块数据
     * @return Block
     */
    public Block generateNextBlock(String blockData) {
        Block previousBlock = this.getLatestBlock();
        int nextIndex = previousBlock.getIndex() + 1;
        long nextTimestamp = System.currentTimeMillis();
        String nextHash = calculateHash(nextIndex, previousBlock.getHash(), nextTimestamp, blockData);
        return new Block(nextIndex, previousBlock.getHash(), nextTimestamp, blockData, nextHash);
    }

    /**
     * 增加新的区块到现有的链上
     * @param newBlock 新区块
     */
    public void addBlock(Block newBlock) {
        if (isValidNewBlock(newBlock, getLatestBlock())) {
            blockChain.add(newBlock);
        }
    }

    /**
     * 是不是有效的新区块
     * @param newBlock 交易打包的区块数据
     * @param previousBlock 上一区块
     * @return true 有效， false 无效
     */
    private boolean isValidNewBlock(Block newBlock, Block previousBlock) {
        if (previousBlock.getIndex() + 1 != newBlock.getIndex()) {
            logger.info("无效的 index");
            return false;
        } else if (!previousBlock.getHash().equals(newBlock.getPreviousHash())) {
            logger.info("无效的上一hash :{}", newBlock.getPreviousHash());
            return false;
        } else {
            String hash = calculateHash(newBlock.getIndex(), newBlock.getPreviousHash(), newBlock.getTimestamp(), newBlock.getData());
            if (!hash.equals(newBlock.getHash())) {
                logger.info("无效的 hash: " + hash + " " + newBlock.getHash());
                return false;
            }
        }
        return true;
    }

    /**
     * 替换区块链
     * @param newBlocks 新区块链
     */
    public void replaceChain(List<Block> newBlocks) {
        if (isValidBlocks(newBlocks) && newBlocks.size() > blockChain.size()) {
            blockChain = newBlocks;
        } else {
            logger.info("接收到无效的区块链");
        }
    }

    /**
     * 是不是有效的区块链
     * @param newBlocks 新的链
     * @return true 有效, false 无效
     */
    private boolean isValidBlocks(List<Block> newBlocks) {
        Block firstBlock = newBlocks.get(0);
        if (firstBlock.equals(getFirstBlock())) {
            return false;
        }
        for (int i = 1; i < newBlocks.size(); i++) {
            if (isValidNewBlock(newBlocks.get(i), firstBlock)) {
                firstBlock = newBlocks.get(i);
            } else {
                return false;
            }
        }
        return true;
    }

    public List<Block> getBlockChain() {
        return blockChain;
    }
}
