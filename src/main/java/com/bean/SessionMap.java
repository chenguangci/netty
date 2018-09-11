package com.bean;

import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存所有session
 */
public class SessionMap {
    public static final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
}
