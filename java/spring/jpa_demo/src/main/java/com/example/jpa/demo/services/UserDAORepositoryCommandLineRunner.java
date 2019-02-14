package com.example.jpa.demo.services;

import com.example.jpa.demo.entity.User;
import com.example.jpa.demo.respositories.UserDAORespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserDAORepositoryCommandLineRunner implements CommandLineRunner {

    @Autowired
    private UserDAORespository userDAORespository;

    private static final Logger log = LoggerFactory.getLogger(UserDAORepositoryCommandLineRunner.class);

    @Override
    public void run(String... args) throws Exception {
        User user = new User("Jack", "Admin");

        long id = userDAORespository.insert(user);

        log.info("New user is created: " + user);

    }
}
