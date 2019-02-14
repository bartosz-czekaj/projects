package com.example.jpa.demo.respositories;

import com.example.jpa.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository  extends JpaRepository<User, Long> {
}
