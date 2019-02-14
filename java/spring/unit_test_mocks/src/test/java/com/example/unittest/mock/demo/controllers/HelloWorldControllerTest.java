package com.example.unittest.mock.demo.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = HelloWorldController.class)
public class HelloWorldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void helloWorld_basic() throws Exception {

        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                                                .get("/hello-world")
                                                .accept(MediaType.APPLICATION_JSON);

        MvcResult result =  mockMvc
                .perform(requestBuilder)
                .andReturn();

        assertEquals("Hello World", result.getResponse().getContentAsString());
    }


    @Test
    public void helloWorld_with_expect() throws Exception {

        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                                        .get("/hello-world")
                                        .accept(MediaType.APPLICATION_JSON);

        MvcResult result =  mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andReturn();
    }
}
