package com.rest.api.example.services;

import org.springframework.stereotype.Service;

@Service
public class WelcomeService {
    public String retrieveWelcomeMessage() {
        return "It's alive 21!!!";
    }
}
