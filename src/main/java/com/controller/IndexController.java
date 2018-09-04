package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class IndexController {

    @RequestMapping(value = "/index")
    public String index() {
        return "/WEB-INF/view/chat.html";
    }

    @RequestMapping(value = "/test")
    public String test() {
        return "index";
    }
}
