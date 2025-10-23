package com.example.fruitstore.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "donhang") // ✅ đúng với tên bảng bạn có
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String maDonHang;

    private LocalDateTime ngayTao;

    private String trangThai;

    private String ghiChu;

    private Double tongTien;

    private Long khachHangId;

    private Long phuongThucThanhToanId;

    private Long khuyenMaiId;

    // ✅ Liên kết tới bảng chi tiết đơn hàng
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetailEntity> chiTietDonHang;
}
