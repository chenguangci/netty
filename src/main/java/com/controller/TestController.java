package com.controller;

import com.bean.ChatResp;
import com.bean.ToUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

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
        System.out.println("接收到的消息+" + user.toString());
        System.out.println(this.messagingTemplate.getUserDestinationPrefix());
        this.messagingTemplate.convertAndSend("/topic/getResponse", user.getMessage());
        this.messagingTemplate.convertAndSendToUser("123456", "/msg", "from server" + user.getMessage());
    }
}
