package org.naivechain.block.transaction;

import java.io.Serializable;

/**
 * 消息
 *
 * @author kangshaofei
 * @date 2019-08-19
 */
public class Message implements Serializable {

    /**
     * 消息类型
     */
    private String type;

    /**
     * 区块消息
     */
    private String data;

    public Message() {
    }

    public Message(String type) {
        this.type = type;
    }

    public Message(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
