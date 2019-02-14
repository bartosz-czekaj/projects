package com.example.spring.boot.conf.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class SpringBootConfExampleApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext run = SpringApplication.run(SpringBootConfExampleApplication.class, args);
    }

    @Profile("dev")
    @Bean
    public String dummy() {
        return "dummy string";
    }
}
