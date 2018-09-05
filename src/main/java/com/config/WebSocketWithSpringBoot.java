package com.config;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/webSocket")
public class WebSocketWithSpringBoot {

    private static ConcurrentHashMap<String, WebSocketWithSpringBoot> webSocketSet = new ConcurrentHashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String id = "";

    @OnOpen
    public void open(HttpServletRequest request, Session session) {
        System.out.println("###########开启请求###########");
        HttpSession httpSession = request.getSession();
        this.id = (String) httpSession.getAttribute("name");
        this.session = session;
        webSocketSet.put(id, this);
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            System.out.println("连接异常");
        }
    }

    @OnClose
    public void onClose() {
        System.out.println("###########关闭请求###########");
        webSocketSet.remove(getId());  //从set中删除
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        //可以自己约定字符串内容，比如 内容|0 表示信息群发，内容|X 表示信息发给id为X的用户
        String sendMessage = message.split("[|]")[0];
        String sendUserId = message.split("[|]")[1];
        try {
            if (webSocketSet.get(sendUserId) != null) {
                if(!id.equals(sendUserId))
                    webSocketSet.get(sendUserId).sendMessage( "用户" + id + "发来消息：" + " <br/> " + sendMessage);
                else
                    webSocketSet.get(sendUserId).sendMessage(sendMessage);
            } else {
                webSocketSet.get(id).sendMessage(sendMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    private String getId() {
        return id;
    }

}
