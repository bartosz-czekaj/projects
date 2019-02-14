package com.example.unittest.mock.demo.services;

import com.example.unittest.mock.demo.models.Item;
import com.example.unittest.mock.demo.repositories.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemBusinessServiceTest {

    @InjectMocks
    ItemBusinessService itemBusinessService = new ItemBusinessService();

    @MockBean
    ItemRepository itemRepositoryMock = mock(ItemRepository.class) ;

    @Test
    public void retrieveAllItems() {
        when(itemRepositoryMock.findAll()).thenReturn(Arrays.asList(
                new Item(1,"Item1", 20,30),
                new Item(2,"Item3", 10,40)
        ));

        itemBusinessService.retrieveAllItems().stream()
                .forEach(item->{
                    assertEquals(item.getValue(), item.getQuantity()*item.getPrice());
                });
    }
}
