package com.example.fruitstore.controller.admin;

import com.example.fruitstore.entity.discountEntity;
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

    @GetMapping
    public String showDiscounts(Model model) {
        List<discountEntity> discounts = discountService.getAllDiscounts();
        model.addAttribute("discounts", discounts);
        model.addAttribute("view", "admin/products/manage_discount");
        return "admin/layout/main";
    }

    // API lấy thông tin khuyến mãi theo id (AJAX)
    @GetMapping("/api/get/{id}")
    @ResponseBody
    public ResponseEntity<discountEntity> getDiscountById(@PathVariable Integer id) {
        return discountService.getDiscountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // API cập nhật khuyến mãi theo id (AJAX)
    @PutMapping("/api/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateDiscount(@PathVariable Integer id, @RequestBody discountEntity update) {
        try {
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
