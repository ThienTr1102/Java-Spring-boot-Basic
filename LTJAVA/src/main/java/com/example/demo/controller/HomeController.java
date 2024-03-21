package com.example.demo.controller;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//Tat ca cac lop controller deu phai tao o package controller va co tu khoa @Controller
@Controller
//Tất cả request <==> url/link localhost:8080/ Đều được control này nắm và xử lý
//Request: get/post/update.delete/put...
@RequestMapping("/")

public class HomeController {
    @GetMapping
    public String home(){ return "home/index"; }

    @GetMapping("/contact")
    public  String contact(){ return "home/contact";}

    @GetMapping("/info")
    public  String info(){ return "home/info";}
    @PostMapping("/submit")
    public String submitContactForm(@ModelAttribute("contact") Contact contact, Model model) {

        model.addAttribute("name", contact.getName());
        model.addAttribute("email", contact.getEmail());
        model.addAttribute("message", contact.getMessage());

        return "home/submit";
    }
    public class Contact {
        private String name;
        private String email;
        private String message;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        // Getter và Setter cho thuộc tính email
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }

        // Getter và Setter cho thuộc tính message
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
