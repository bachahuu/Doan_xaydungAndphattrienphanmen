package com.example.fruitstore.controller.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.fruitstore.entity.discountEntity;
import com.example.fruitstore.entity.cart.cartDetailEntity;
import com.example.fruitstore.entity.cart.cartEntity;
import com.example.fruitstore.service.cartService;
import com.example.fruitstore.service.discountService;

import jakarta.persistence.criteria.CriteriaBuilder.In;

@Controller
public class dashboard {
    @Autowired
    private cartService cartService;
    @Autowired
    private discountService discountService;

    private Integer getCurrentUserId() {
        return 1;
    };

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
        // 1. Lấy ID khách hàng hiện tại (giả sử là 1)
        Integer currentUserId = getCurrentUserId();
        cartEntity usercart = cartService.getCartByKhachHangId(currentUserId);

        double tamtinh = 0.0;
        if (usercart != null && usercart.getCartDetail() != null) {
            for (cartDetailEntity detail : usercart.getCartDetail()) {
                tamtinh += detail.getSoLuong() * detail.getSanPham().getGia();
            }
        }
        // 3. Đưa tổng tiền ("Tạm tính") vào Model
        model.addAttribute("cartTotalPrice", tamtinh);
        // 2. Lấy danh sách khuyến mãi từ service
        List<discountEntity> activeDiscounts = discountService.getAllDiscounts().stream()
                .filter(d -> d.getTrangThai() == 1) // Lọc những mã còn hoạt động
                .collect(Collectors.toList());
        model.addAttribute("discounts", activeDiscounts);
        model.addAttribute("view", "user/products/location");
        return "user/layout/main";
    }

    @GetMapping("/payment")
    public String showPayment(Model model) {
        model.addAttribute("view", "user/products/payment");
        return "user/layout/main";
    }

}
