package com.example.fruitstore.entity.cart;

import lombok.Data;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.example.fruitstore.entity.SanPham;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@Table(name = "chitietgiohang")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "gioHang" })
public class cartDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY) // quan hệ nhiều - 1 với đơn hàng
    @JoinColumn(name = "gioHangId", nullable = false)
    @JsonBackReference
    private cartEntity cart;
    // mỗi chi tiết giỏ hàng gắn với 1 sản phẩm
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sanPhamId")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private SanPham sanPham;
    @Column(name = "soLuong")
    private Integer soLuong;
}
