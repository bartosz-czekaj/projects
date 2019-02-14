package com.example.spring.boot.conf.demo.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByRole(@Param("role") String role);

}
