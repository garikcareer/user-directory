package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService companyService) {
        this.userService = companyService;
    }

    // Create
    @GetMapping("/add")
    public ModelAndView addUser(Model model) {
        model.addAttribute("content", "add-user");
        model.addAttribute("pageTitle", "Add User");
        model.addAttribute("user", new User());
        return new ModelAndView("layout");
    }

    // Read
    @GetMapping
    @ModelAttribute
    public ModelAndView users(Model model) {
        model.addAttribute("content", "users");
        model.addAttribute("pageTitle", "Users");
        model.addAttribute("companies", userService.getUsers());
        return new ModelAndView("layout");
    }

    // Update
    @PostMapping("/update/{id}")
    public ModelAndView updateUser(Model model, @PathVariable Long id) {
        model.addAttribute("content", "add-user");
        model.addAttribute("pageTitle", "Edit User");
        model.addAttribute("user", userService.getById(id));
        return new ModelAndView("layout");
    }

    // Delete
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }
}
