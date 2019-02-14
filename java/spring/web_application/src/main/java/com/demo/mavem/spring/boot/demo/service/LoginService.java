package com.demo.mavem.spring.boot.demo.service;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
    public boolean validateUser(String userId, String password) {
        return userId.equalsIgnoreCase("user") && password.equalsIgnoreCase("pass");
    }
}
