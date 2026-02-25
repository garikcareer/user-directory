package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserRestController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void addUser_ShouldReturn200() throws Exception {
        String json = "{\"name\":\"John\", \"location\":\"NY\", \"email\":\"john@test.com\"}";

        mockMvc.perform(post("/api/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void getAllUsers_ShouldReturnList() throws Exception {
        User u1 = new User(); u1.setName("John");
        User u2 = new User(); u2.setName("Jane");
        when(userService.getUsers()).thenReturn(Arrays.asList(u1, u2));
        mockMvc.perform(get("/api/users/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message[0].name").value("John"))
                .andExpect(jsonPath("$.message[1].name").value("Jane"));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Alice");
        when(userService.getById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/get")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message.name").value("Alice"));
    }

    @Test
    void updateUser_ShouldReturn200() throws Exception {
        String json = "{\"name\":\"John Updated\", \"location\":\"LA\", \"email\":\"john@test.com\"}";
        mockMvc.perform(put("/api/users/update/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User with ID (1) updated successfully."))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void deleteUser_ShouldReturn200() throws Exception {
        mockMvc.perform(delete("/api/users/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User with ID (1) deleted successfully"));
    }
}
