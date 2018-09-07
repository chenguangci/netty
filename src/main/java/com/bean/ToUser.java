package com.bean;

/**
 * 封装消息实体
 */
public class ToUser {
    private int fromId;
    private String message;
    private int toId;

    @Override
    public String toString() {
        return "ToUser{" +
                "fromId=" + fromId +
                ", message='" + message + '\'' +
                ", toId=" + toId +
                '}';
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }
}
