package com.ferraro.JobPlatform.controller;

import com.ferraro.JobPlatform.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.adminAPI}")
@Slf4j
public class AdminController {

    @Autowired
    private UserService userService;






}
