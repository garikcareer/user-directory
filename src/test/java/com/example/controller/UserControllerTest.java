package com.example.controller;

import com.example.model.User;
import com.example.service.AppUserDetailsService;
import com.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@WithMockUser
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AppUserDetailsService appUserDetailsService;

    // --- List / search ---

    @Test
    void users_ShouldShowUserList() throws Exception {
        Page<User> emptyPage = new PageImpl<>(Collections.emptyList());
        when(userService.searchUsersPage(anyString(), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("content", "users"))
                .andExpect(model().attributeExists("userList"));
    }

    @Test
    void users_WithSearchParam_ShouldPassSearchToModel() throws Exception {
        User user = new User("Alice Johnson", "New York, NY", "alice@example.com");
        Page<User> resultPage = new PageImpl<>(List.of(user));
        when(userService.searchUsersPage(eq("Alice"), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(resultPage);

        mockMvc.perform(get("/users").param("search", "Alice"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("search", "Alice"))
                .andExpect(model().attributeExists("userList"));
    }

    @Test
    void users_WithSearchParam_FirstResultMatchesSearch() throws Exception {
        User matched = new User("Alice Johnson", "New York, NY", "alice@example.com");
        Page<User> resultPage = new PageImpl<>(List.of(matched));
        when(userService.searchUsersPage(eq("alice"), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(resultPage);

        mockMvc.perform(get("/users").param("search", "alice"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userList", List.of(matched)));
    }

    @Test
    void users_WithEmptySearch_ShouldReturnAll() throws Exception {
        Page<User> page = new PageImpl<>(List.of(new User(), new User()));
        when(userService.searchUsersPage(eq(""), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(page);

        mockMvc.perform(get("/users").param("search", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userList"));
    }

    // --- Add / Edit forms ---

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

    // --- Save / Delete ---

    @Test
    void saveUser_ShouldRedirectToUsers() throws Exception {
        mockMvc.perform(post("/users/save")
                        .with(csrf())
                        .param("name", "New User")
                        .param("location", "Test City")
                        .param("email", "test@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    void deleteUser_ShouldRedirectToUsers() throws Exception {
        mockMvc.perform(delete("/users/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }
}
