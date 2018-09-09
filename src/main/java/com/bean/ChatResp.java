package com.bean;

import javax.security.auth.Subject;
import java.security.Principal;

public class ChatResp implements Principal {
    private String msg;
    private String from;

    public ChatResp() {
    }

    public ChatResp(String msg, String from) {
        this.msg = msg;
        this.from = from;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }
}
