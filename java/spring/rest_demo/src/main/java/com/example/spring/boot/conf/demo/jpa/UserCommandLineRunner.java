package com.example.spring.boot.conf.demo.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.StreamSupport;

@Component
public class UserCommandLineRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory
            .getLogger(UserCommandLineRunner.class);

    @Autowired
    private UserRestRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        userRepository.save(new User("admin1", "Admin"));
        userRepository.save(new User("admin2", "Admin"));
        userRepository.save(new User("user1", "User"));
        userRepository.save(new User("user2", "User"));

        log.info("-------------------------------");
        log.info("Finding all users");
        log.info("-------------------------------");
        StreamSupport.stream(userRepository.findAll().spliterator(), false).forEach(user -> log.info(user.toString()));
    }

}
