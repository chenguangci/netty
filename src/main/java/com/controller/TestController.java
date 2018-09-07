package com.controller;

import com.bean.ToUser;
import com.config.WebSocketURLConfig;
import com.service.MessageManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageManagement management;

    @MessageMapping("/welcome")
    @SendTo(WebSocketURLConfig.RECEIVE_SERVER + "/getResponse")
    public void greeting(ToUser user) {
        //处理消息
        System.out.println("接收到的消息" + user.toString());
        this.messagingTemplate.convertAndSendToUser(String.valueOf(user.getToId()), "/msg", "from "+ user.getFromId() +" " + user.getMessage());
    }

    /**
     * 触发系统推送功能
     * 前端页面订阅topic/server
     */
    @MessageMapping("/getServerMsg")
    @SendTo(WebSocketURLConfig.RECEIVE_SERVER + "/server")
    public void serverMsg() {
        Thread thread = new Thread(() -> {
            while (true) {
                this.messagingTemplate.convertAndSend(WebSocketURLConfig.RECEIVE_SERVER + "/server", "server msg");
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
