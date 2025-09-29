package com.example.fruitstore.entity.cart;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
@Table(name = "giohang")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class cartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "maGioHang")
    private String maGioHang;
    @Column(name = "khachHangId")
    private Integer khachHangId;
    @Column(name = "ngayTao")
    private Date ngayTao;

    @OneToMany(mappedBy = "gioHang", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<cartDetailEntity> chiTietGioHang;
}
