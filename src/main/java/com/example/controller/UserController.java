package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    private static final int PAGE_SIZE = 10;

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView users(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "") String search) {

        Page<User> userPage = userService.searchUsersPage(search, page, PAGE_SIZE, sortField, sortDir);

        ModelAndView modelAndView = new ModelAndView("layout");
        modelAndView.addObject("content", "users");
        modelAndView.addObject("pageTitle", "Users");
        modelAndView.addObject("userList", userPage.getContent());
        modelAndView.addObject("userPage", userPage);
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", userPage.getTotalPages());
        modelAndView.addObject("totalElements", userPage.getTotalElements());
        modelAndView.addObject("sortField", sortField);
        modelAndView.addObject("sortDir", sortDir);
        modelAndView.addObject("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");
        modelAndView.addObject("search", search);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView showAddUserForm() {
        ModelAndView modelAndView = new ModelAndView("layout");
        modelAndView.addObject("content", "user-form");
        modelAndView.addObject("pageTitle", "Add User");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("layout");
        modelAndView.addObject("content", "user-form");
        modelAndView.addObject("pageTitle", "Edit User");
        modelAndView.addObject("user", userService.getById(id));
        return modelAndView;
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user) {
        if (user.getId() == null) {
            userService.add(user);
        } else {
            userService.update(user);
        }
        return "redirect:/users";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }
}