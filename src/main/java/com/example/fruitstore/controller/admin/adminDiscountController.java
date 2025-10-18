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
    private discountRepository repository;  // Đổi tên để tránh trùng với class

    @GetMapping
    public String showDiscounts(Model model) {
        List<discountEntity> discounts = discountService.getAllDiscounts();
        model.addAttribute("discounts", discounts);
        model.addAttribute("view", "admin/products/manage_discount");
        return "admin/layout/main";
    }

    @PostMapping("/add")
    public String addDiscount(@ModelAttribute discountEntity discount, Model model) {
        try {
            // Kiểm tra mã khuyến mãi đã tồn tại
            if (repository.existsByMaKM(discount.getMaKM())) {
                model.addAttribute("addError", "Mã khuyến mãi đã tồn tại!");
                model.addAttribute("discounts", discountService.getAllDiscounts());
                model.addAttribute("view", "admin/products/manage_discount");
                return "admin/layout/main";
            }
            
            // Validation
            if (discount.getGiaTri() < 0 || discount.getGiaTri() > 100) {
                model.addAttribute("addError", "Giá trị giảm giá chỉ được từ 0 đến 100!");
                model.addAttribute("discounts", discountService.getAllDiscounts());
                model.addAttribute("view", "admin/products/manage_discount");
                return "admin/layout/main";
            }
            
            if (discount.getGiaTriDonHangToiThieu() == null || discount.getGiaTriDonHangToiThieu() < 0) {
                model.addAttribute("addError", "Giá trị đơn hàng tối thiểu không được âm!");
                model.addAttribute("discounts", discountService.getAllDiscounts());
                model.addAttribute("view", "admin/products/manage_discount");
                return "admin/layout/main";
            }
            
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
            if (discount.getNgayBatDau().before(today)) {
                model.addAttribute("addError", "Ngày bắt đầu không được trước ngày hiện tại!");
                model.addAttribute("discounts", discountService.getAllDiscounts());
                model.addAttribute("view", "admin/products/manage_discount");
                return "admin/layout/main";
            }
            
            if (discount.getNgayKetThuc().before(today)) {
                model.addAttribute("addError", "Ngày kết thúc không được trước ngày hiện tại!");
                model.addAttribute("discounts", discountService.getAllDiscounts());
                model.addAttribute("view", "admin/products/manage_discount");
                return "admin/layout/main";
            }
            
            if (discount.getNgayKetThuc().before(discount.getNgayBatDau())) {
                model.addAttribute("addError", "Ngày kết thúc không được trước ngày bắt đầu!");
                model.addAttribute("discounts", discountService.getAllDiscounts());
                model.addAttribute("view", "admin/products/manage_discount");
                return "admin/layout/main";
            }
            
            // Lưu khuyến mãi
            repository.save(discount);
            return "redirect:/admin/discount";
        } catch (Exception e) {
            model.addAttribute("addError", e.getMessage());
            model.addAttribute("discounts", discountService.getAllDiscounts());
            model.addAttribute("view", "admin/products/manage_discount");
            return "admin/layout/main";
        }
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