package com.example.fruitstore.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.example.fruitstore.entity.order.orderEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "khuyenmai")
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "orders", "sanPhams" })
public class discountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "maKM")
    private String maKM;

    @Column(name = "tenKM")
    private String tenKM;

    @Column(name = "giaTri")
    private Double giaTri;

    @Column(name = "giaTriDonHangToiThieu")
    private Double giaTriDonHangToiThieu;

    @Column(name = "ngayBatDau")
    private java.sql.Date ngayBatDau;

    @Column(name = "ngayKetThuc")
    private java.sql.Date ngayKetThuc;

    @Column(name = "moTa", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "trangThai")
    private Integer trangThai; // 0 = Hết hiệu lực, 1 = Đang áp dụng, 2 = Chưa áp dụng

    // Mối quan hệ
    @OneToMany(mappedBy = "discount", fetch = FetchType.LAZY)
    private List<orderEntity> orders;

    @OneToMany(mappedBy = "khuyenMai", fetch = FetchType.LAZY)
    private List<SanPham> sanPhams;
}