package com.example.fruitstore.controller.user;

// import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.fruitstore.service.cartService;

import jakarta.servlet.http.HttpSession;

import com.example.fruitstore.entity.CustomerEntity;
import com.example.fruitstore.entity.loginCustomerEntity;
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
    public ResponseEntity<?> addProductToCart(
            @RequestParam("productId") Integer productId,
            @RequestParam("quantity") Integer quantity, HttpSession session) {

        // Lấy đối tượng loginCustomerEntity từ session
        loginCustomerEntity loginCustomer = (loginCustomerEntity) session.getAttribute("customer");
        // kiểm tra xem người dùng đã đăng nhập chưa
        if (loginCustomer == null) {
            return new ResponseEntity<>("Vui lòng đăng nhập để thêm sản phẩm.", HttpStatus.UNAUTHORIZED);
        }
        // Lấy thông tin CustomerEntity liên quan và ID của khách hàng
        CustomerEntity customer = loginCustomer.getCustomer();
        if (customer == null) {
            return new ResponseEntity<>("Lỗi: Dữ liệu khách hàng không hợp lệ.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Integer khachHangId = customer.getId();

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

    // kiểm tra giỏ hàng của khách hàng hiện tại
    @GetMapping("/check-empty")
    public ResponseEntity<String> checkCartEmpty(HttpSession session) {
        // Lấy đối tượng loginCustomerEntity từ session
        loginCustomerEntity loginCustomer = (loginCustomerEntity) session.getAttribute("customer");
        // kiểm tra xem người dùng đã đăng nhập chưa
        if (loginCustomer == null) {
            return new ResponseEntity<>("Vui lòng đăng nhập để kiểm tra giỏ hàng.", HttpStatus.UNAUTHORIZED);
        }
        CustomerEntity customer = loginCustomer.getCustomer();
        if (customer == null) {
            return new ResponseEntity<>("Lỗi: Dữ liệu khách hàng không hợp lệ.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Integer khachHangId = customer.getId();
        cartEntity cart = cartService.getCartByKhachHangId(khachHangId);

        if (cart == null || cart.getCartDetail() == null || cart.getCartDetail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Giỏ hàng của bạn đang trống.");
        }

        return ResponseEntity.ok("Giỏ hàng hợp lệ.");
    }

}
