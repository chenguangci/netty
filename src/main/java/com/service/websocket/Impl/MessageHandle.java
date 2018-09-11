package com.service.websocket.Impl;

import com.bean.ToUser;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MessageHandle {

    //模拟缓存
    private Map<String, List<ToUser>> messageCache = new HashMap<>();

    /**
     * 异步调用消息存储
     * @param user
     */
    @Async
    public void saveMsg(ToUser user) {
        String userId = user.getToId();
        //TODO 存储消息
        if (messageCache.get(userId) == null) {
            List<ToUser> list = new ArrayList<>();
            list.add(user);
            messageCache.put(userId, list);
        } else {
            messageCache.get(userId).add(user);
        }
    }

    List<ToUser> getUnReadMsg(String userId) {
        //TODO 获取未读消息，同时更新状态
        List<ToUser> list =  messageCache.get(userId);
        messageCache.remove(userId);
        return list;
    }

    public void getMsg(WebSocketSession session) {

    }

}
