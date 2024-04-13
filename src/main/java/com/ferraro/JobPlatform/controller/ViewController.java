package com.ferraro.JobPlatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/employer/test")
    public String pagina(){
        return "pagetest";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
