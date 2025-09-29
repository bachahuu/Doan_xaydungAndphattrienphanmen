package com.example.fruitstore.entity.order;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

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
    private orderEntity donHang;
    @Column(name = "sanPhamId")
    private Integer sanPhamId;
    @Column(name = "soLuong")
    private Integer soLuong;
    @Column(name = "gia")
    private BigDecimal gia;
}
