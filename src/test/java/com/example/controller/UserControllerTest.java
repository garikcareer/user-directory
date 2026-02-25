package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void users_ShouldShowUserList() throws Exception {
        Page<User> emptyPage = new PageImpl<>(Collections.emptyList());
        when(userService.getUsersPage(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("content", "users"))
                .andExpect(model().attributeExists("userList"));
    }

    @Test
    void showAddUserForm_ShouldShowForm() throws Exception {
        mockMvc.perform(get("/users/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("content", "user-form"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void showEditForm_ShouldShowForm() throws Exception {
        when(userService.getById(anyLong())).thenReturn(new User());
        mockMvc.perform(get("/users/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("content", "user-form"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void saveUser_ShouldRedirectToUsers() throws Exception {
        mockMvc.perform(post("/users/save")
                        .param("name", "New User")
                        .param("location", "Test City")
                        .param("email", "test@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    void deleteUser_ShouldRedirectToUsers() throws Exception {
        mockMvc.perform(delete("/users/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }
}
