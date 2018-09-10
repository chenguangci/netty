package com.controller;

import com.bean.SessionMap;
import com.dto.Result;
import com.bean.ToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class IndexController {

    @RequestMapping(value = "/")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/test")
    public String test() {
        return "webSocket";
    }

    @RequestMapping(value = "/index")
    public ModelAndView index(@RequestParam("name")String name, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("userId", name);
        ModelAndView view = new ModelAndView("webSocket");
        view.addObject("userId", name);
        return view;
    }

    @RequestMapping(value = "/updateList", method = RequestMethod.GET)
    @ResponseBody
    public Result updateList(@SessionAttribute(value = "userId")String id) {
        List<ToUser> users = new ArrayList<>();
        for (Map.Entry<String, WebSocketSession> entry : SessionMap.sessionMap.entrySet()) {
            if (!id.equals(entry.getKey())) {
                ToUser user = new ToUser();
                user.setFromId(id);
                user.setToId((String) entry.getValue().getAttributes().get("userId"));
                users.add(user);
            }
        }
        return new Result<>(users, true);
    }
}
