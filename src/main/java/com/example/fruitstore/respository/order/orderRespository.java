package com.example.fruitstore.respository.order;

import org.springframework.stereotype.Repository;
import com.example.fruitstore.entity.order.orderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface orderRespository extends JpaRepository<orderEntity, Integer> {
    Optional<orderEntity> findByMaDonHang(String maDonHang);

    // Thêm phương thức để tìm đơn hàng theo ID khách hàng
    List<orderEntity> findByKhachHangId(Integer khachHangId);

    List<orderEntity> findByNgayTaoBetween(LocalDateTime start, LocalDateTime end);

    // Tự động tạo câu lệnh SQL: WHERE maDonHang LIKE %maDonHang%
    List<orderEntity> findByMaDonHangContaining(String maDonHang);

}
