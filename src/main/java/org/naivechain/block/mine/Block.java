package org.naivechain.block.mine;

/**
 * 区块数据结构
 *
 * @author kangshaofei
 * @date 2019-08-18
 */
public class Block {

    /**
     * 区块索引
     */
    private int index;

    /**
     * 上一区块hash
     */
    private String previousHash;

    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * 交易数据
     */
    private String data;

    /**
     * 本区块hash
     */
    private String hash;

    /**
     * 随机数 挖矿时需要计算出此随机数来
     */
    private int randomNumber;


    public Block() {
    }

    public Block(int index, String previousHash, long timestamp, String data, String hash) {
        this.index = index;
        this.previousHash = previousHash;
        this.timestamp = timestamp;
        this.data = data;
        this.hash = hash;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(int randomNumber) {
        this.randomNumber = randomNumber;
    }

    @Override
    public String toString() {
        return "Block{" +
                "index=" + index +
                ", previousHash='" + previousHash + '\'' +
                ", timestamp=" + timestamp +
                ", data='" + data + '\'' +
                ", hash='" + hash + '\'' +
                ", randomNumber=" + randomNumber +
                '}';
    }
}

