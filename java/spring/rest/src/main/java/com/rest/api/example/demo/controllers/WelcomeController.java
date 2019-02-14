package com.rest.api.example.demo.controllers;

import com.rest.api.example.services.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @Autowired
    private WelcomeService welcomeService;

    @RequestMapping("/welcome")
    public String welcome() {
        return welcomeService.retrieveWelcomeMessage();
    }


}
