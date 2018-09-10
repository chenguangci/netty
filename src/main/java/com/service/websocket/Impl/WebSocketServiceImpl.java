package com.service.websocket.Impl;

import com.bean.SessionMap;
import com.bean.ToUser;
import com.service.websocket.WebSocketService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    /*
     * 缓存的session连接
     */
    private Map<String, WebSocketSession> map = SessionMap.sessionMap;

    @Override
    public void addSession(WebSocketSession session) {
        String userId = (String) session.getAttributes().get("userId");
        map.put(userId, session);
        System.out.println("session连接开始：" + userId);
    }

    @Override
    public void removeSession(WebSocketSession session) {
        String userId = (String) session.getAttributes().get("userId");
        map.remove(userId);
    }

    @Override
    public void sendToUser(ToUser user, WebSocketSession session, TextMessage message) throws IOException {
        WebSocketSession target = map.get(user.getToId());
        if (target != null) {
            target.sendMessage(message);
        } else {
            user.setMessage("对方不在线");
            user.setFromId("server");
            session.sendMessage(new TextMessage(JSONObject.fromObject(user).toString()));
        }
    }

    @Override
    public void sendToAllUsers(TextMessage message) throws IOException {
        for (Map.Entry<String, WebSocketSession> entry : map.entrySet())
            entry.getValue().sendMessage(message);
    }
}
