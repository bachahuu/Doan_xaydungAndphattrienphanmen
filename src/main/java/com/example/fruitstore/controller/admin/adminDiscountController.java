package com.example.fruitstore.controller.admin;

import com.example.fruitstore.entity.discountEntity;
import com.example.fruitstore.respository.discountRepository;
import com.example.fruitstore.service.discountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/discount")
public class adminDiscountController {
    @Autowired
    private discountService discountService;
    
    @Autowired
    private discountRepository repository;

    @GetMapping
    public String showDiscounts(Model model) {
        // **TỰ ĐỘNG CẬP NHẬT TẤT CẢ TRẠNG THÁI TRƯỚC KHI HIỂN THỊ**
        List<discountEntity> discounts = discountService.getAllDiscounts();
        model.addAttribute("discounts", discounts);
        model.addAttribute("view", "admin/products/manage_discount");
        return "admin/layout/main";
    }

    @PostMapping("/add")
    public String addDiscount(@ModelAttribute discountEntity discount, Model model) {
        try {
            // **TỰ ĐỘNG CẬP NHẬT TRẠNG THÁI TRONG SERVICE**
            discountService.addDiscount(discount);
            model.addAttribute("addSuccess", "Thêm khuyến mãi thành công!");
            model.addAttribute("discounts", discountService.getAllDiscounts());
            model.addAttribute("view", "admin/products/manage_discount");
            return "admin/layout/main";
        } catch (Exception e) {
            model.addAttribute("addError", e.getMessage());
            model.addAttribute("discounts", discountService.getAllDiscounts());
            model.addAttribute("view", "admin/products/manage_discount");
            return "admin/layout/main";
        }
    }

    // API lấy thông tin khuyến mãi theo id (AJAX) - **TỰ ĐỘNG CẬP NHẬT**
    @GetMapping("/api/get/{id}")
    @ResponseBody
    public ResponseEntity<discountEntity> getDiscountById(@PathVariable Integer id) {
        return discountService.getDiscountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // API cập nhật khuyến mãi theo id (AJAX) - **TỰ ĐỘNG CẬP NHẬT**
    @PutMapping("/api/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateDiscount(@PathVariable Integer id, @RequestBody discountEntity update) {
        try {
            // **TỰ ĐỘNG CẬP NHẬT TRẠNG THÁI TRONG SERVICE**
            discountEntity updated = discountService.updateDiscount(id, update);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/api/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteDiscount(@PathVariable Integer id) {
        try {
            discountService.deleteDiscount(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}