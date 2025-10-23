package com.example.fruitstore.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.example.fruitstore.entity.SanPham;
import com.example.fruitstore.entity.DanhMuc;
import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    // Find products by danhMuc id via the relation (danhMuc.id)
    java.util.List<SanPham> findByDanhMuc_Id(Integer danhMucId);

    @Query("SELECT p FROM SanPham p LEFT JOIN FETCH p.danhMuc d LEFT JOIN FETCH p.nhaCungCap n WHERE p.id = :id")
    SanPham findByIdWithDetails(@Param("id") Integer id);

    boolean existsByDanhMuc(DanhMuc danhMuc);

    List<SanPham> findByTenSanPhamContainingIgnoreCase(String tenSanPham);
}