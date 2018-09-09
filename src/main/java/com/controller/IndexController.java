package com.controller;

import com.dto.Result;
import com.bean.SessionIdMap;
import com.bean.ToUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class IndexController {

    @Autowired
    private SimpUserRegistry registry;

    @RequestMapping(value = "/")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/test")
    public String test() {
        return "index";
    }

    @RequestMapping(value = "/index")
    public ModelAndView index(@RequestParam("name")String name, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("id", name);
        SessionIdMap.sessionIds.add(Integer.valueOf(name));
        ModelAndView view = new ModelAndView("chat");
        view.addObject("userId", name);
        return view;
    }

    @RequestMapping(value = "/updateList", method = RequestMethod.GET)
    @ResponseBody
    public Result updateList(@SessionAttribute(value = "id")int id) {
        System.out.println(registry.getUserCount());
        List<ToUser> users = new ArrayList<>();
        for (int toId : SessionIdMap.sessionIds) {
//            if (toId != id) {
                ToUser user = new ToUser();
                user.setToId(toId);
                user.setFromId(id);
                users.add(user);
//            }
        }
        return new Result<>(users, true);
    }
}
