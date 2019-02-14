package com.example.unittest.mock.demo.repositories;

import com.example.unittest.mock.demo.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {

}
