package com.example.fruitstore.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class adminDashboard {
    @GetMapping("/admin/order")
    public String showorder(Model model) {
        model.addAttribute("view", "admin/products/manage_order");
        return "admin/layout/main";
    }

    @GetMapping("/admin/product")
    public String showproduct(Model model) {
        model.addAttribute("view", "admin/products/manage_product");
        return "admin/layout/main";
    }

    @GetMapping("/admin/discount")
    public String showdiscount(Model model) {
        model.addAttribute("view", "admin/products/manage_discount");
        return "admin/layout/main";
    }

    @GetMapping("/admin/category")
    public String showcategory(Model model) {
        model.addAttribute("view", "admin/products/pro");
        return "admin/layout/main";
    }

}
