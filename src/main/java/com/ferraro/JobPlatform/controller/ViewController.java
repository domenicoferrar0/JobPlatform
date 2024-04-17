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

    @GetMapping("/home/registration")
    public String signUp(){
        return "user-registration";
    }

    @GetMapping("/home/employers/registration")
    public String employerSignUp(){
        return "employer-registration";
    }
    @GetMapping("/home/confirm")
    public String confirm(){
        return "confirmation";
    }

    @GetMapping("/user/home")
    public String userHome(){
        return "user-home";
    }

    @GetMapping("/user/logout")
    public String logout(){
        return "logout";
    }
}
