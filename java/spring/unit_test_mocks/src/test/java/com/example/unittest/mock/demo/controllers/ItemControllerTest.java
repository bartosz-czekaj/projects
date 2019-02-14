package com.example.unittest.mock.demo.controllers;

import com.example.unittest.mock.demo.models.Item;
import com.example.unittest.mock.demo.services.ItemBusinessService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemBusinessService itemBusinessServiceMock;

    @Test
    public void dummyItem_json_whole_response() throws Exception {
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .get("/dummy-item")
                .accept(MediaType.APPLICATION_JSON);

        final String expected = "{\"id\" : 1,\"name\":\"Ball\",\"price\":10,\"quantity\":100}";

        MvcResult result =  mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(expected))
                .andReturn();

    }

    @Test
    public void dummyItem_json_whole_response_json_assert_strict_true() throws Exception {
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .get("/dummy-item")
                .accept(MediaType.APPLICATION_JSON);

        final String expected = "{\"id\":1,\"name\":\"Ball\",\"price\":10,\"quantity\":100,\"value\":0}";

        MvcResult result =  mockMvc
                .perform(requestBuilder)
                .andReturn();

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
    }

    @Test
    public void dummyItem_json_only_one_element() throws Exception {
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .get("/dummy-item")
                .accept(MediaType.APPLICATION_JSON);

        final String expected = "{\"price\":10}";

        MvcResult result =  mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(expected))
                .andReturn();

    }

    @Test
    public void dummyItem_json_only_one_element_json_assert_strict_false() throws Exception {
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .get("/dummy-item")
                .accept(MediaType.APPLICATION_JSON);

        final String expected = "{\"price\":10}";

        MvcResult result =  mockMvc
                .perform(requestBuilder)
                .andReturn();

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }


    @Test
    public void item_from_business_service() throws Exception {
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .get("/item-from-business-service")
                .accept(MediaType.APPLICATION_JSON);

        final String expected = "{\"id\":2,\"name\":\"Keyboard\",\"price\":12,\"quantity\":34}";

        when(itemBusinessServiceMock.retrieveHardcodedItem()).thenReturn(new Item(2,"Keyboard", 12, 34));

        MvcResult result =  mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(expected))
                .andReturn();
    }

    @Test
    public void retrieve_all_items() throws Exception {

        when(itemBusinessServiceMock.retrieveAllItems()).thenReturn(Arrays.asList(
                new Item(2, "Item2", 10, 10),
                new Item(3, "Item4", 20, 40)
        ));

        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .get("/all-items-from-database")
                .accept(MediaType.APPLICATION_JSON);

        final String expected = "[{\"id\":2,\"name\":\"Item2\",\"price\":10,\"quantity\":10,\"value\":0},{\"id\":3,\"name\":\"Item4\",\"price\":20,\"quantity\":40,\"value\":0}]";

        MvcResult result =  mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(expected))
                .andReturn();
    }
}
