package com.bean;

/**
 * 封装消息实体
 */
public class ToUser {
    private String fromId;
    private String message;
    private String toId;

    @Override
    public String toString() {
        return "{" +
                "fromId:" + fromId +
                ", message:'" + message + '\'' +
                ", toId:" + toId +
                '}';
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }
}
