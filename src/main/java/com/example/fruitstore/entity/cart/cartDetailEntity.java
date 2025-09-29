package com.example.fruitstore.entity.cart;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gioHangId", nullable = false)
    @JsonBackReference
    private cartEntity gioHang;
    @Column(name = "sanPhamId")
    private Integer sanPhamId;
    @Column(name = "soLuong")
    private Integer soLuong;
}
