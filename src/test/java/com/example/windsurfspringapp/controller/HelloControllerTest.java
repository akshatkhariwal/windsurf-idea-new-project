package com.example.windsurfspringapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void homeEndpointShouldReturnWelcomeMessage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome to your Spring Boot application!")));
    }

    @Test
    public void helloEndpointShouldReturnDefaultGreeting() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, World!")));
    }

    @Test
    public void helloEndpointShouldReturnCustomGreeting() throws Exception {
        String name = "Spring";
        mockMvc.perform(get("/hello").param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, Spring!")));
    }

    @Test
    public void healthEndpointShouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Application is running successfully!")));
    }
}
