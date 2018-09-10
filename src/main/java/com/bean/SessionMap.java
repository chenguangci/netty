package com.bean;

import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionMap {
    public static Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
}
