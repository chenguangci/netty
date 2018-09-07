package com.controller;

import com.bean.ChatResp;
import com.bean.ToUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class TestController {

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/ws/chat")
    public void handleChat(Principal principal, String msg) {
        String destUser = msg.substring(msg.lastIndexOf(";") + 1, msg.length());
        String message = msg.substring(0, msg.lastIndexOf(";"));
        messagingTemplate.convertAndSendToUser(destUser, "/queue/chat", new ChatResp(message, principal.getName()));
    }

    @MessageMapping("/welcome")
    @SendTo("/topic/getResponse")
    public void greeting(ToUser user) {
        //处理消息
        System.out.println("接收到的消息" + user.toString());
        System.out.println(this.messagingTemplate.getUserDestinationPrefix());
//        this.messagingTemplate.convertAndSend("/topic/getResponse", user.getMessage());
        this.messagingTemplate.convertAndSendToUser(String.valueOf(user.getToId()), "/msg", "from "+ user.getFromId() +" " + user.getMessage());
    }

    /**
     * 触发系统推送功能
     * 前端页面订阅topic/server
     */
    @MessageMapping("/getServerMsg")
    @SendTo("/topic/server")
    public void serverMsg() {
        Thread thread = new Thread(() -> {
            while (true) {
                this.messagingTemplate.convertAndSend("/topic/server", "server msg");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        thread.start();
    }
}
