package com.example.fruitstore.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.fruitstore.entity.supplierEntity;
import com.example.fruitstore.service.supplierService;

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

    // @GetMapping("/admin/category")
    // public String showcategory(Model model) {
    // model.addAttribute("view", "admin/products/pro");
    // return "admin/layout/main";
    // }
    @Autowired
    private supplierService supplierService;

    @GetMapping("/admin/supplier")
    public String showSupplierList(Model model) {
        List<supplierEntity> suppliers = supplierService.getAllSuppliers();
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("view", "admin/products/manage_supplier");
        return "admin/layout/main";
    }

}
