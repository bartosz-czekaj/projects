package com.demo.mavem.spring.boot.demo.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class Helper {
    public static String getLoggedinUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails)authentication.getPrincipal();

        if(principal != null) {
            return principal.getUsername();
        }

        return authentication.getPrincipal().toString();
    }
}
