package com.example.fruitstore.controller.admin;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.fruitstore.entity.CustomerEntity;
import com.example.fruitstore.service.CustomerService;

@RestController
@RequestMapping("/admin/customer")
public class AdminCustomerController {

    @Autowired
    private CustomerService customerService;

    // Lấy danh sách khách hàng dưới dạng JSON
    @GetMapping("/list")
    public List<CustomerEntity> getAllCustomers() {
        return customerService.getAllCustomers();
    }
}
