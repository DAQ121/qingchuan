package com.qingchuan.coderman.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@SuppressWarnings("all")
public class ChatController {
    //聊天
    @GetMapping("/chat")
    public String chat(Map<String,Object> map) {
        map.put("navLi","chat");
        return "chat";
    }
}
