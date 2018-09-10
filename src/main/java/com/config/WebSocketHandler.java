package com.config;

import com.bean.SessionMap;
import com.bean.ToUser;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 消息处理的核心类
 */
public final class WebSocketHandler extends TextWebSocketHandler {

    /*
     * 换成session连接
     */
    private Map<String, WebSocketSession> map = SessionMap.sessionMap;

    /**
     * 建立连接
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        System.out.println("建立连接后的userId：" + userId);
        map.put(userId, session);
        System.out.println("session连接开始：" + userId);
    }

    /**
     * 处理消息
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println(message.getPayload());
        ToUser user = (ToUser) JSONObject.toBean(JSONObject.fromObject(message.getPayload()), ToUser.class);

        if (String.valueOf(user.getFromId()).equals(SystemConfig.adminId)) {    //管理员推送系统消息
            sendToAll(new TextMessage(user.getMessage()));
        } else {
            boolean isSend = sendToUser(user.getToId(), new TextMessage(user.toString()));
            if (!isSend) {
                user.setMessage("对方不在线");
                session.sendMessage(new TextMessage(user.toString()));
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("连接关闭：" + session.getId());
        String userId = (String) session.getAttributes().get("userId");
        map.remove(userId);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 发送给指定用户
     */
    private boolean sendToUser(String userId, TextMessage message) throws IOException {
        WebSocketSession session = map.get(userId);
        if (session != null) {
            System.out.println(userId + " " + session.getAttributes().get("userId"));
            session.sendMessage(message);
            return true;
        } else {
            return false;
        }
//        sendToAll(message);
//        return true;
    }

    /**
     * 发送给所有在线用户
     */
    private void sendToAll(TextMessage message) throws IOException {
        System.out.println(map.size());
        for (Map.Entry<String, WebSocketSession> entry : map.entrySet())
            entry.getValue().sendMessage(message);
    }
}
