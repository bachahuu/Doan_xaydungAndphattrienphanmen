package com.example.fruitstore.respository;

import com.example.fruitstore.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

  // =================== 💰 DOANH THU ===================
  @Query("""
          SELECT SUM(o.tongTien)
          FROM OrderEntity o
          WHERE o.ngayTao BETWEEN :start AND :end
          AND o.trangThai = 'HoanThanh'
      """)
  Double sumTotalByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  @Query(value = """
          SELECT CAST(SUM(tongTien) AS DECIMAL(20,2))
          FROM donhang
          WHERE YEAR(ngayTao) = :year AND MONTH(ngayTao) = :month
            AND trangThai = 'HoanThanh'
      """, nativeQuery = true)
  BigDecimal sumTotalByMonth(@Param("year") int year, @Param("month") int month);

  @Query(value = """
          SELECT CAST(SUM(tongTien) AS DECIMAL(20,2))
          FROM donhang
          WHERE YEAR(ngayTao) = :year
            AND trangThai = 'HoanThanh'
      """, nativeQuery = true)
  BigDecimal sumTotalByYear(@Param("year") int year);

  @Query("""
          SELECT SUM(o.tongTien)
          FROM OrderEntity o
          WHERE o.ngayTao BETWEEN :from AND :to
            AND o.trangThai = 'HoanThanh'
      """)
  Double sumTotalByRange(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

  @Query(value = """
          SELECT
              sp.tenSanPham AS tenSanPham,
              SUM(ct.soLuong) AS tongSoLuong,
              ct.gia AS donGia,
              SUM(ct.soLuong * ct.gia) AS tongTien
          FROM chitietdonhang ct
          JOIN sanpham sp ON ct.sanPhamId = sp.id
          JOIN donhang o ON o.id = ct.donHangId
          WHERE o.ngayTao BETWEEN :from AND :to
            AND o.trangThai = 'HoanThanh'
          GROUP BY sp.tenSanPham, ct.gia
          ORDER BY tongTien DESC
      """, nativeQuery = true)
  List<Object[]> getSoldProductsBetween(@Param("from") LocalDateTime from,
      @Param("to") LocalDateTime to);

  // =================== 📦 ĐƠN HÀNG ===================
  @Query("""
          SELECT COUNT(o)
          FROM OrderEntity o
          WHERE o.ngayTao BETWEEN :start AND :end
            AND o.trangThai = 'HoanThanh'
      """)
  Integer countByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  @Query(value = """
          SELECT CAST(COUNT(*) AS SIGNED)
          FROM donhang
          WHERE YEAR(ngayTao) = :year AND MONTH(ngayTao) = :month
            AND trangThai = 'HoanThanh'
      """, nativeQuery = true)
  Integer countByMonth(@Param("year") int year, @Param("month") int month);

  @Query(value = """
          SELECT CAST(COUNT(*) AS SIGNED)
          FROM donhang
          WHERE YEAR(ngayTao) = :year
            AND trangThai = 'HoanThanh'
      """, nativeQuery = true)
  Integer countByYear(@Param("year") int year);
}
