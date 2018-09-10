package com.config;

import com.bean.ToUser;
import net.sf.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebSocketHandler extends TextWebSocketHandler {

    /*
     * 换成session连接
     */
    private List<WebSocketSession> sessions = new ArrayList<>();

    /**
     * 建立连接
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        String username = (String) session.getAttributes().get("userId");
        System.out.println("session连接开始：" + username);
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
        //管理员推送系统消息
        if (String.valueOf(user.getFromId()).equals(SystemConfig.adminId)) {
            sendToAll(new TextMessage(user.getMessage()));
        } else {
            boolean isSend = sendToUser(user.getToId(), new TextMessage(user.getMessage()));
            if (isSend) {
                session.sendMessage(new TextMessage("对方不在线"));
            }
        }
        session.sendMessage(new TextMessage(message.getPayload().toString()));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("连接关闭：" + session.getId());
        sessions.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 发送给指定用户
     */
    private boolean sendToUser(String userId, TextMessage message) throws IOException {
        boolean isSend = false;
        for (WebSocketSession session : sessions) {
            if (userId.equals(session.getId())) {
                session.sendMessage(message);
                isSend = true;
                break;
            }
        }
        return isSend;
    }

    /**
     * 发送给所有在线用户
     */
    private void sendToAll(TextMessage message) throws IOException {
        for (WebSocketSession session : sessions)
            session.sendMessage(message);
    }
}
