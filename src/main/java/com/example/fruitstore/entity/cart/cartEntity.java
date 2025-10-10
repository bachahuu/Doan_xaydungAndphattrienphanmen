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
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true) // quan hệ 1 - nhiều với chi tiết giỏ
    @JsonManagedReference
    private List<cartDetailEntity> cartDetail;
    @Column(name = "khachHangId")
    private Integer khachHangId;
    @Column(name = "ngayTao")
    private Date ngayTao;
}
