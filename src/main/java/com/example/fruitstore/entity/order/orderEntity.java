package com.example.fruitstore.entity.order;

import lombok.Data;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;

import com.example.fruitstore.entity.CustomerEntity;
import com.example.fruitstore.entity.discountEntity;
import com.example.fruitstore.entity.phuongThucThanhToan;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    @Column(name = "maDonHang")
    private String maDonHang;
    // Quan hệ
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) // quan hệ 1 - nhiều với chi tiết
    // đơn hàng
    @JsonManagedReference
    private List<orderDetailEntity> orderDetail;
    // cascade = CascadeType.ALL : khi xoá đơn hàng thì sẽ xoá luôn chi tiết đơn
    // hàng tương ứng
    // mỗi đơn hàng gắn với 1 khách hàng
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khachHangId")
    private CustomerEntity khachHang;

    // mỗi đơn hàng gắn với 1 phương thức thanh toán
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phuongThucThanhToanId")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private phuongThucThanhToan phuongThucThanhToan;

    // mỗi đơn hàng gắn với 1 khuyến mãi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khuyenMaiId")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "orders", "sanPhams" })
    private discountEntity discount;

    @Column(name = "tongTien")
    private BigDecimal tongTien;
    @Column(name = "ngayTao")
    private Date ngayTao;
    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai")
    private TrangThai trangThai;
    @Column(name = "ghiChu")
    private String ghiChu;
    @Column(name = "tenNguoiNhan")
    private String tenNguoiNhan;
    @Column(name = "soDienThoaiNguoiNhan")
    private String soDienThoaiNguoiNhan;
    @Column(name = "diaChiGiaoHang")
    private String diaChiGiaoHang;

    public enum TrangThai {
        ChoXuLy,
        XacNhan,
        DangGiao,
        HoanThanh,
        DaHuy
    }

    public String getTrangThaiDisplay() {
        switch (this.trangThai) {
            case ChoXuLy:
                return "Chờ xử lý";
            case XacNhan:
                return "Xác nhận";
            case DangGiao:
                return "Đang giao";
            case HoanThanh:
                return "Hoàn tất";
            case DaHuy:
                return "Đã hủy";
            default:
                return "Không xác định";
        }
    }

}
