package com.example.fruitstore.controller.user;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;

@Controller
public class dashboard {

    @GetMapping("/menu")
    public String showMenu(Model model) {
        model.addAttribute("view", "user/products/menu");
        return "user/layout/main";
    }

    @GetMapping("/home")
    public String showHome(Model model) {
        model.addAttribute("view", "user/products/home");
        return "user/layout/main";
    }

    @GetMapping("/cart")
    public String showCart(Model model) {
        model.addAttribute("view", "user/products/cart");
        return "user/layout/main";
    }

    @GetMapping("/contact")
    public String showContact(Model model) {
        model.addAttribute("view", "user/products/contact");
        return "user/layout/main";
    }

    @GetMapping("/about")
    public String showAbout(Model model) {
        model.addAttribute("view", "user/products/about");
        return "user/layout/main";
    }

    @GetMapping("/location")
    public String showlocation(Model model) {
        model.addAttribute("view", "user/products/location");
        return "user/layout/main";
    }

}
