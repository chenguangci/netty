package com.service.websocket;

import com.bean.ToUser;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public interface WebSocketService {

    /**
     * 添加session
     */
    void addSession(WebSocketSession session) throws IOException;

    /**
     * 移除session
     */
    void removeSession(WebSocketSession session);

    /**
     * 发送消息给指定用户
     */
    void sendToUser(ToUser user, WebSocketSession session) throws IOException;

    /**
     * 发送消息给所有用户
     */
    void sendToAllUsers(ToUser user) throws IOException;

}
