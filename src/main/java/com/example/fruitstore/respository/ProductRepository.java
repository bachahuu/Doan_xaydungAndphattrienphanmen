package com.example.fruitstore.respository;

import com.example.fruitstore.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<SanPham, Integer> {

    // 📦 Lấy tên sản phẩm và số lượng tồn
    @Query("SELECT p.tenSanPham, p.soLuongTon FROM SanPham p")
    List<Object[]> getInventory();

    // ✅ Đếm tổng số sản phẩm tồn kho (cộng dồn số lượng tồn)
    @Query("SELECT SUM(p.soLuongTon) FROM SanPham p")
    Integer countProductsInStock();
}
