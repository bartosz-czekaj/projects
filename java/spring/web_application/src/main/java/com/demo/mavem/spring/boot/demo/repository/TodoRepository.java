package com.demo.mavem.spring.boot.demo.repository;

import com.demo.mavem.spring.boot.demo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findByUser(String user);
}
