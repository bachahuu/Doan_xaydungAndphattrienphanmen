package com.example.fruitstore.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.fruitstore.service.cartService;
import com.example.fruitstore.entity.cart.cartDetailEntity;
import com.example.fruitstore.entity.cart.cartEntity;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final cartService cartService;

    public CartController(cartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<cartEntity> getAllCarts() {
        return cartService.getAllCarts();
    }

    @GetMapping("/{id}")
    public cartEntity getCartById(@PathVariable Integer id) {
        return cartService.getCartById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<cartDetailEntity> addProductToCart(
            @RequestParam("productId") Integer productId,
            @RequestParam("quantity") Integer quantity) {

        Integer khachHangId = 1;// chỗ này phải thay thế bằng ID người dùng thực tế sau khi có hệ thống đăng
                                // nhập
        try {
            // Gọi đến service với đủ 3 tham số
            cartDetailEntity addedItem = cartService.addCart(khachHangId, productId, quantity);
            return new ResponseEntity<>(addedItem, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Bắt lỗi nếu không tìm thấy sản phẩm
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/detail/{chiTietId}")

    public Double updateSoLuong(@PathVariable Integer chiTietId, @RequestParam Integer soLuong) {
        return cartService.updateSoLuongSanPham(chiTietId, soLuong);
    }

    /*
     * Xóa toàn bộ giỏ hàng và tất cả chi tiết giỏ hàng liên quan
     */
    @DeleteMapping("/{id}")
    public String deleteCart(@PathVariable Integer id) {
        return cartService.deleteCart(id);
    }

    /*
     * Xóa một sản phẩm cụ thể khỏi giỏ hàng
     */
    @DeleteMapping("/detail/{chiTietId}")
    public void removeProductFromCart(@PathVariable Integer chiTietId) {
        cartService.removeSanPham(chiTietId);
    }
}
