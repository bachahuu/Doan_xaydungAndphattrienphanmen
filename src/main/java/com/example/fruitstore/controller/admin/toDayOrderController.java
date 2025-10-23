package com.example.fruitstore.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;

import com.example.fruitstore.entity.order.orderEntity;
import com.example.fruitstore.service.order.orderService;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class toDayOrderController {

    @Autowired
    private orderService orderService;

    @GetMapping("/admin/order/today")
    public String showorder(Model model) {
        List<orderEntity> orders = orderService.getTodaysOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("view", "admin/products/toDay_order");
        return "admin/layout/main";
    }

}
