package com.example.myapp.controller;

import com.example.myapp.model.User;
import com.example.myapp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDB() {
        userRepository.deleteAll();
    }

    @Test
    void testCreateUserEndpoint() throws Exception {
        User user = new User(null, "John", "john.doe@example.com");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User Created Successfully"))
                .andExpect(jsonPath("$.data.name").value("John"))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.traceId").exists());

    }

    @Test
    void testGetAllUsersEndpoint() throws Exception {
        // Arrange: Save test users to the database
        userRepository.saveAll(List.of(
                new User(null, "Alice", "alice@example.com"),
                new User(null, "Bob", "bob@example.com")
        ));

        // Describe your expected result
        String expectedMessage = "Users fetched successfully";

        // Act: Perform GET request and capture the result
        var mvcResult = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(expectedMessage))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name", anyOf(is("Alice"), is("Bob"))))
                .andExpect(jsonPath("$.data[1].name", anyOf(is("Alice"), is("Bob"))))
                .andExpect(jsonPath("$.traceId").exists())
                .andReturn(); // 👈 capture the full response

        // Log the request and response for debugging/visibility
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        System.out.println("📥 Request: GET /api/users");
        System.out.println("📤 Expected Message: " + expectedMessage);
        System.out.println("📤 Expected Response: " + mvcResult);
        System.out.println("📡 Actual Response Body: " + actualResponseBody);
    }

}
