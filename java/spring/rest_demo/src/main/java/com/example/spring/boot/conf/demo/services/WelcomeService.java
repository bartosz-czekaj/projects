package com.example.spring.boot.conf.demo.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WelcomeService {

    @Value("${welcome.message}")
    private String melcomeMessage;

    public String retrieveWelcomeMessage() {
        return melcomeMessage;
    }
}
