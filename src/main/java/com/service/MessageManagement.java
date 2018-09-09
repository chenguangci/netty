package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

/**
 * 处理消息逻辑
 */
@Service
public class MessageManagement {

    @Autowired
    private SimpUserRegistry registry;

    public void test() {
        System.out.println(registry.getUsers());
    }

}
