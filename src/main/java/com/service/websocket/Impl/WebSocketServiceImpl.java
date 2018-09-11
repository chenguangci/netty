package com.service.websocket.Impl;

import com.bean.SessionMap;
import com.bean.ToUser;
import com.service.websocket.WebSocketService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private MessageHandle messageHandle;

    /*
     * 缓存的session连接
     */
    private Map<String, WebSocketSession> map = SessionMap.sessionMap;

    @Override
    public void addSession(WebSocketSession session) throws IOException {
        String userId = (String) session.getAttributes().get("userId");
        map.put(userId, session);
        System.out.println("session连接开始：" + userId);
        //检索是否有未读消息，有就发送
        List<ToUser> list = messageHandle.getUnReadMsg(userId);
        if (list!=null)
            for (ToUser user : list)
                send(user, session);
    }

    @Override
    public void removeSession(WebSocketSession session) {
        String userId = (String) session.getAttributes().get("userId");
        map.remove(userId);
    }

    @Override
    public void sendToUser(ToUser user, WebSocketSession session) throws IOException {
        WebSocketSession target = map.get(user.getToId());
        if (target != null && target.isOpen()) {
            user.setRead(true);
//            messageHandle.saveMsg(user);
            send(user, target);
        } else {
            //对方不在线，缓存
            user.setRead(false);
            messageHandle.saveMsg(user);
            send(user, session);
        }
    }

    /**
     * 发送公告
     * @param user
     * @throws IOException
     */
    @Override
    public void sendToAllUsers(ToUser user) throws IOException {
        for (Map.Entry<String, WebSocketSession> entry : map.entrySet())
            send(user, entry.getValue());
    }

    /**
     * 发送消息
     * @param user 消息实体
     * @param session 指定的session
     * @throws IOException
     */
    private void send(ToUser user, WebSocketSession session) throws IOException {
        JSONObject object = JSONObject.fromObject(user);
        session.sendMessage(new TextMessage(object.toString()));
    }


}
