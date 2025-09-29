package com.example.fruitstore.controller.user;

import org.springframework.web.bind.annotation.*;
import com.example.fruitstore.service.cartService;
import com.example.fruitstore.entity.cart.cartEntity;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class cartController {
    private final cartService cartService;

    public cartController(cartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<cartEntity> getAllCarts() {
        return cartService.getAllCarts();
    }

    @GetMapping("/{maGioHang}")
    public cartEntity getCartByMaGioHang(@PathVariable String maGioHang) {
        return cartService.getCartByMaGioHang(maGioHang);
    }

    @PostMapping
    public cartEntity createCart(@RequestParam Integer khachHangId) {
        return cartService.addCart(khachHangId);
    }

    /*
     * Xóa toàn bộ giỏ hàng và tất cả chi tiết giỏ hàng liên quan
     */
    @DeleteMapping("/{maGioHang}")
    public String deleteCart(@PathVariable String maGioHang) {
        return cartService.deleteCart(maGioHang);
    }

    /*
     * Xóa một sản phẩm cụ thể khỏi giỏ hàng
     */
    @DeleteMapping("/detail/{chiTietId}")
    public void removeProductFromCart(@PathVariable Integer chiTietId) {
        cartService.removeSanPham(chiTietId);
    }
}
