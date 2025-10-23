package com.example.fruitstore.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fruitstore.entity.SanPham;
import com.example.fruitstore.service.SanPhamService;

@RestController
@RequestMapping("api/admin/product")
public class adminProductApiController {

    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping("/list")
    public List<SanPham> getAllProducts() {
        return sanPhamService.getAllSanPham();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SanPham> getProduct(@PathVariable Integer id) {
        SanPham p = sanPhamService.getSanPhamById(id);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody SanPham product) {
        // **Quan trọng:** Không cần set ngayNhap ở đây.
        // SanPham.java sẽ tự động điền ngày giờ hiện tại nhờ @PrePersist nếu ngayNhap là NULL.
        try {
            // Đảm bảo id là null để tạo mới
            product.setId(null); 
            SanPham saved = sanPhamService.save(product);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi thêm sản phẩm: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody SanPham product) {
        SanPham existing = sanPhamService.getSanPhamById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        // 1. Cập nhật các trường đơn giản
        existing.setMaSanPham(product.getMaSanPham());
        existing.setTenSanPham(product.getTenSanPham());
        existing.setGia(product.getGia());
        existing.setMoTa(product.getMoTa());
        existing.setHinhAnh(product.getHinhAnh());
        existing.setSoLuongTon(product.getSoLuongTon());
        
        // 2. Cập nhật 2 trường ngày tháng mới <-- THÊM MỚI
        // Spring sẽ tự động parse nếu dữ liệu gửi lên hợp lệ (chuỗi ISO 8601).
        existing.setNgayNhap(product.getNgayNhap()); 
        existing.setHanSuDung(product.getHanSuDung()); 

        // 3. Cập nhật các mối quan hệ (nếu API gọi đến bao gồm chúng)
        if (product.getNhaCungCap() != null) {
            existing.setNhaCungCap(product.getNhaCungCap());
        }
        if (product.getDanhMuc() != null) {
            existing.setDanhMuc(product.getDanhMuc());
        }

        try {
            SanPham saved = sanPhamService.save(existing);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        try {
            sanPhamService.deleteById(id);
            return ResponseEntity.ok().body(java.util.Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", e.getMessage()));
        }
    }
}