package com.example.fruitstore.controller.admin;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.fruitstore.entity.order.orderEntity;
import com.example.fruitstore.service.orderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import com.example.fruitstore.entity.order.orderEntity.TrangThai;

@RestController
@RequestMapping("api/admin/order")
public class adminOrderController {
    @Autowired
    private orderService orderService;

    @GetMapping("/list")
    public List<orderEntity> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/detail/{maDonHang}")
    public ResponseEntity<orderEntity> getOrderDetail(@PathVariable String maDonHang) {
        try {
            orderEntity order = orderService.getOrderByMaDonHang(maDonHang);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    // API update trạng thái
    @PutMapping("/update-status/{maDonHang}")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable String maDonHang,
            @RequestParam TrangThai trangThai) {
        try {
            orderService.updateOrderStatus(maDonHang, trangThai);
            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi: " + e.getMessage());
        }
    }

}
