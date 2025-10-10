package com.example.fruitstore.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.example.fruitstore.entity.order.orderEntity;

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

    @Column(name = "moTa", columnDefinition = "TEXT") // Bổ sung trường còn thiếu
    private String moTa;

    @Column(name = "trangThai")
    private Integer trangThai;

    // moi quan he
    @OneToMany(mappedBy = "discount", fetch = FetchType.LAZY)
    private List<orderEntity> orders;

    @OneToMany(mappedBy = "discount", fetch = FetchType.LAZY)
    private List<SanPham> sanPhams;
}
