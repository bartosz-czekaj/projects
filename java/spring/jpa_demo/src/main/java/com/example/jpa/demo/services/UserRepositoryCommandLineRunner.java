package com.example.jpa.demo.services;

import com.example.jpa.demo.entity.User;
import com.example.jpa.demo.respositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryCommandLineRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserRepositoryCommandLineRunner.class);

    @Override
    public void run(String... args) throws Exception {
        User user = new User("Jack_1", "Admin");

        User userCreated = userRepository.save(user);
        
        Optional<User> userWithIdOne = userRepository.findById(1L);
        final List<User> allUsers = userRepository.findAll();

        log.info("New user is created: " + userCreated);

    }
}
