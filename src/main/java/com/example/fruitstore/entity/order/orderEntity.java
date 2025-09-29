package com.example.fruitstore.entity.order;

import lombok.Data;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "donhang")
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class orderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, orphanRemoval = true) // quan hệ 1 - nhiều với chi tiết
                                                                                      // đơn hàng
    private List<orderDetailEntity> chiTietDonHang;
    // cascade = CascadeType.ALL : khi xoá đơn hàng thì sẽ xoá luôn chi tiết đơn
    // hàng tương ứng

    @Column(name = "maDonHang")
    private String maDonHang;
    @Column(name = "khachHangId")
    private Integer khachHangId;
    @Column(name = "phuongThucThanhToanId")
    private Integer phuongThucThanhToanId;
    @Column(name = "khuyenMaiId")
    private Integer khuyenMaiId;
    @Column(name = "tongTien")
    private BigDecimal tongTien;
    @Column(name = "ngayTao")
    private Date ngayTao;
    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai")
    private TrangThai trangThai;
    @Column(name = "ghiChu")
    private String ghiChu;
    @Column(name = "diaChiGiaoHang")
    private String diaChiGiaoHang;

    public enum TrangThai {
        ChoXuLy,
        XacNhan,
        DangGiao,
        HoanThanh,
        DaHuy
    }

}
