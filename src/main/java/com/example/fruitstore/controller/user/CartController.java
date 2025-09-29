package com.example.fruitstore.controller.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.fruitstore.entity.SanPham;
import com.example.fruitstore.repository.SanPhamRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
    @Autowired
    private SanPhamRepository sanPhamRepository;

    @PostMapping("/cart/add")
    @ResponseBody
    public Map<String, Object> addToCart(@RequestParam("productId") Integer productId,
                                         @RequestParam("quantity") Integer quantity,
                                         HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        SanPham product = sanPhamRepository.findById(productId).orElse(null);
        if (product == null) {
            response.put("success", false);
            response.put("message", "Sản phẩm không tồn tại!");
            return response;
        }
        if (product.getSoLuongTon() == null || product.getSoLuongTon() == 0) {
            response.put("success", false);
            response.put("message", "Sản phẩm đã hết hàng!");
            return response;
        }
        if (quantity > product.getSoLuongTon()) {
            response.put("success", false);
            response.put("message", "Không thể thêm quá số lượng tồn kho!");
            return response;
        }
        // Lưu giỏ hàng trong session
        Object cartObj = session.getAttribute("cart");
        Map<Integer, Integer> cart;
        if (cartObj instanceof Map<?, ?>) {
            try {
                @SuppressWarnings("unchecked")
                Map<Integer, Integer> safeCart = (Map<Integer, Integer>) cartObj;
                cart = safeCart;
            } catch (ClassCastException e) {
                cart = new HashMap<>();
            }
        } else {
            cart = new HashMap<>();
        }
        int currentQty = cart.getOrDefault(productId, 0);
        if (currentQty + quantity > product.getSoLuongTon()) {
            response.put("success", false);
            response.put("message", "Không thể thêm quá số lượng tồn kho!");
            return response;
        }
        cart.put(productId, currentQty + quantity);
        session.setAttribute("cart", cart);
        response.put("success", true);
        response.put("message", "Đã thêm vào giỏ hàng!");
        return response;
    }
}
