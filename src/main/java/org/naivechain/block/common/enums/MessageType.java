package org.naivechain.block.common.enums;

/**
 * 消息类型
 */
public enum MessageType {

    QUERY_LAST_BLOCK_CHAIN("0", "查询最后一个区块"),
    QUERY_ALL_BLOCK_CHAIN("1", "查询所有区块"),
    RESPONSE_BLOCK_CHAIN("2", "响应区块");


    /**
     * 消息码
     */
    private String code;

    /**
     * 消息码描述
     */
    private String description;

    MessageType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
