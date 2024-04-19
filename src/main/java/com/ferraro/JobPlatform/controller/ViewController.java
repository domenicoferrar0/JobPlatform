package com.ferraro.JobPlatform.controller;

import com.ferraro.JobPlatform.configuration.JwtFilter;
import com.ferraro.JobPlatform.service.JwtService;
import com.ferraro.JobPlatform.service.RedirectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ViewController {

    @Autowired
    private RedirectService redirectService;

    @GetMapping("/employer/test")
    public String pagina(){
        return "pagetest";
    }

    @GetMapping("/login")
    public String login(@CookieValue(value ="token", required = false) String authorization){
        if(!StringUtils.hasText(authorization) || !authorization.startsWith("Bearer")){
        return "/login";
        }
        System.out.println("\n \n \nUSER LOGGATO, GESTIONE REDIRECT \n \n \n");
        return "redirect:"+redirectService.redirectIfLogged(authorization);
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

    @GetMapping("/employer/appliance/file/{applianceId}")
    public String applianceFile(@PathVariable("applianceId") String applianceId){
        System.out.println("\n \n ENDPOINT PAGINA FILE");
        return "file-shower";
    }

    @GetMapping("/employer/home")
    public String employerHome(){
        return "employer-home";
    }
}
