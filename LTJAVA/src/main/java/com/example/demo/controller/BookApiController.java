package com.example.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bookapi")
public class BookApiController {
    @GetMapping
    public String showAllBook(Model model){
        model.addAttribute("title", "Book List - Call Api");
        return "book/list_api";
    }

}
