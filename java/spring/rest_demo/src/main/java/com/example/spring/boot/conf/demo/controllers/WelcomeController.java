package com.example.spring.boot.conf.demo.controllers;

import com.example.spring.boot.conf.demo.configuration.BasicConfiguration;
import com.example.spring.boot.conf.demo.services.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class WelcomeController {

    @Autowired
    private WelcomeService welcomeService;

    @Autowired
    private BasicConfiguration basicConfiguration;

    @RequestMapping("/welcome")
    public String welcome() {
        return welcomeService.retrieveWelcomeMessage();
    }


    @RequestMapping("/dynamic-configuration")
    public Map dynamicConfiguration() {
        Map map = new HashMap();

        map.put("message",basicConfiguration.getMessage());
        map.put("number",basicConfiguration.isValue());
        map.put("value",basicConfiguration.getNumber());

        return map;
    }
}
