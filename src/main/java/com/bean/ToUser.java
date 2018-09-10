package com.bean;

import java.util.Date;

/**
 * 封装消息实体
 */
public class ToUser {
    private String fromId;
    private String message;
    private Date time;
    private String toId;

    @Override
    public String toString() {
        return "ToUser{" +
                "fromId='" + fromId + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                ", toId='" + toId + '\'' +
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
