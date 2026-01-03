package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. List Users
    @GetMapping
    public ModelAndView users() {
        ModelAndView modelAndView = new ModelAndView("layout");
        modelAndView.addObject("content", "users");
        modelAndView.addObject("pageTitle", "Users");
        modelAndView.addObject("userList", userService.getUsers());
        return modelAndView;
    }

    // 2. Show Add Form
    @GetMapping("/add")
    public ModelAndView showAddUserForm() {
        ModelAndView modelAndView = new ModelAndView("layout");
        modelAndView.addObject("content", "user-form");
        modelAndView.addObject("pageTitle", "Add User");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    // 3. Show Edit Form
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("layout");
        modelAndView.addObject("content", "user-form");
        modelAndView.addObject("pageTitle", "Edit User");
        modelAndView.addObject("user", userService.getById(id));
        return modelAndView;
    }

    // 4. Save User
    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user) {
        if (user.getId() == null) {
            userService.add(user);
        } else {
            userService.update(user);
        }
        return "redirect:/users";
    }

    // 5. Delete User
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }
}