package com.service.websocket;

import com.bean.ToUser;
import com.config.SystemConfig;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * 消息处理的核心类
 */
@Service
public final class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private WebSocketService webSocketService;

    /**
     * 建立连接
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketService.addSession(session);
    }

    /**
     * 处理消息
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        ToUser user = (ToUser) JSONObject.toBean(JSONObject.fromObject(message.getPayload()), ToUser.class);
        if (String.valueOf(user.getFromId()).equals(SystemConfig.adminId)) {    //管理员推送系统消息
            webSocketService.sendToAllUsers(new TextMessage(user.getMessage()));
        } else {
            webSocketService.sendToUser(user, session, new TextMessage(message.getPayload().toString()));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println(exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        webSocketService.removeSession(session);
    }

}
