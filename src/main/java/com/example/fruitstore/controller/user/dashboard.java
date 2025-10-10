package com.example.fruitstore.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.fruitstore.entity.cart.cartEntity;
import com.example.fruitstore.service.cartService;

@Controller
public class dashboard {
    @Autowired
    private cartService cartService;

    @GetMapping("/menu-static")
    public String showMenu(Model model) {
        model.addAttribute("view", "user/products/menu");
        return "user/layout/main";
    }

    @GetMapping("/home-static")
    public String showHome(Model model) {
        model.addAttribute("view", "user/products/home");
        return "user/layout/main";
    }

    @GetMapping("/cart")
    public String showCart(Model model) {
        List<cartEntity> carts = cartService.getAllCarts();
        model.addAttribute("carts", carts);
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

    @GetMapping("/payment")
    public String showPayment(Model model) {
        model.addAttribute("view", "user/products/payment");
        return "user/layout/main";
    }

}
