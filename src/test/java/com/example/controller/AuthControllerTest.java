package com.example.controller;

import com.example.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    // --- Login page ---

    @Test
    void loginPage_ShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("content", "login"))
                .andExpect(model().attribute("pageTitle", "Login"));
    }

    // --- Register page ---

    @Test
    void registerPage_ShouldReturnRegisterView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("content", "register"))
                .andExpect(model().attribute("pageTitle", "Register"));
    }

    // --- Register POST: success ---

    @Test
    void register_WithValidCredentials_ShouldRedirectToLogin() throws Exception {
        when(authService.usernameExists("newuser")).thenReturn(false);

        mockMvc.perform(post("/register")
                        .param("username", "newuser")
                        .param("password", "securePass1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("success"));
    }

    // --- Register POST: username already taken ---

    @Test
    void register_WithExistingUsername_ShouldRedirectToRegisterWithError() throws Exception {
        when(authService.usernameExists("takenuser")).thenReturn(true);

        mockMvc.perform(post("/register")
                        .param("username", "takenuser")
                        .param("password", "securePass1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("error"));
    }

    // --- Register POST: password too short ---

    @Test
    void register_WithShortPassword_ShouldRedirectToRegisterWithError() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "newuser")
                        .param("password", "abc"))   // less than 6 chars
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("error"));
    }

    // --- Register POST: blank username ---

    @Test
    void register_WithBlankUsername_ShouldRedirectToRegisterWithError() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "   ")
                        .param("password", "securePass1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("error"));
    }
}
