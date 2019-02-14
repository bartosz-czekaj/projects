package com.example.unittest.mock.demo.services;

import com.example.unittest.mock.demo.models.Item;
import com.example.unittest.mock.demo.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemBusinessService {

    @Autowired
    private ItemRepository itemRepository;

    public Item retrieveHardcodedItem() {
        return new Item(2,"Keyboard", 12, 34);
    }

    public List<Item> retrieveAllItems() {
        List<Item> items = itemRepository.findAll();
        items.stream().forEach(item->item.setValue(item.getPrice() * item.getQuantity()));

        return items;
    }

}
