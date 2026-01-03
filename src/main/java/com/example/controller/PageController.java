package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("content", "index");
        model.addAttribute("pageTitle", "Home");
        return "layout";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("content", "about");
        model.addAttribute("pageTitle", "About");
        return "layout";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("content", "contact");
        model.addAttribute("pageTitle", "Contact");
        return "layout";
    }
}