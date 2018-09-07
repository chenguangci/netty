package com.bean;

/**
 * 封装消息实体
 */
public class ToUser {
    private int id;
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ToUser{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}
