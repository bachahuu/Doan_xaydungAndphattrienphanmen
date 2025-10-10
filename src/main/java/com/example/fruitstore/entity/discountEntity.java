package com.example.fruitstore.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "khuyenmai")
@Data
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

    @Column(name = "ngayBatDau")
    private java.sql.Date ngayBatDau;

    @Column(name = "ngayKetThuc")
    private java.sql.Date ngayKetThuc;

    @Column(name = "trangThai")
    private Integer trangThai;
}
