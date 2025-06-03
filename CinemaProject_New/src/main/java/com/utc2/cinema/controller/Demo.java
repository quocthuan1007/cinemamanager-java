package com.utc2.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Demo {
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
}
