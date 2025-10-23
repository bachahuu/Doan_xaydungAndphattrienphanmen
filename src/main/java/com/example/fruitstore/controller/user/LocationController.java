package com.example.fruitstore.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fruitstore.entity.discountEntity;
import com.example.fruitstore.service.discountService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/location")
public class LocationController {
    @Autowired
    private discountService discountService;

    @GetMapping("/discount") // API lấy danh sách khuyến mãi
    public List<discountEntity> getAllDiscounts() {
        return discountService.getAllDiscounts();
    }

}
