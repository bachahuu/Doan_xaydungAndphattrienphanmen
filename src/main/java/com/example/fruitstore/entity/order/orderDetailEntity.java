package com.example.fruitstore.entity.order;

import lombok.Data;

import java.math.BigDecimal;

import com.example.fruitstore.entity.SanPham;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "chitietdonhang")
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "donHang" })
public class orderDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY) // quan hệ nhiều - 1 với đơn hàng
    @JoinColumn(name = "donHangId")
    @JsonBackReference
    private orderEntity order;
    // mỗi chi tiết gắn với 1 sản phẩm
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sanPhamId")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private SanPham sanPham;

    @Column(name = "soLuong")
    private Integer soLuong;
    @Column(name = "gia")
    private BigDecimal gia;
}
