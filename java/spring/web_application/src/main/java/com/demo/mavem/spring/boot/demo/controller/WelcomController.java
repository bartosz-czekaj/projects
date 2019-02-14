package com.demo.mavem.spring.boot.demo.controller;

import com.demo.mavem.spring.boot.demo.helpers.Helper;
import com.demo.mavem.spring.boot.demo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class WelcomController {

    @Autowired
    LoginService loginService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    //@ResponseBody
    public String showLoginPage(/*@RequestParam String name, */ModelMap modelMap) {
        modelMap.put("name", getLoggedinUserName());
        return "welcome";
    }

    private String getLoggedinUserName(){
        return Helper.getLoggedinUserName();
    }

    /*@RequestMapping(value = "/login", method = RequestMethod.POST)
    //@ResponseBody
    public String showWelcomePage(@RequestParam String name, @RequestParam String password, ModelMap modelMap) {

        boolean valid = loginService.validateUser(name, password);
        if(!valid) {
            modelMap.put("message", "Invalid Credentials");
            return "login";
        }
        modelMap.put("name", name);
        modelMap.put("password", password);
        return "welcome";
    }*/
}
