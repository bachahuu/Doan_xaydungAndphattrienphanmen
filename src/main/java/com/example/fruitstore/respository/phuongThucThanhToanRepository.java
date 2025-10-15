package com.example.fruitstore.respository;

import com.example.fruitstore.entity.phuongThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface phuongThucThanhToanRepository extends JpaRepository<phuongThucThanhToan, Integer> {
    List<phuongThucThanhToan> findByTrangThai(int trangThai);
}
