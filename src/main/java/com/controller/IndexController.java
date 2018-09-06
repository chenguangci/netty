package com.controller;

import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class IndexController {

    @RequestMapping(value = "/")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/test")
    public String test() {
        return "index";
    }

    @RequestMapping(value = "/index")
    public String index(@RequestParam("name")String name, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("name", name);
        return "chat";
    }
}
