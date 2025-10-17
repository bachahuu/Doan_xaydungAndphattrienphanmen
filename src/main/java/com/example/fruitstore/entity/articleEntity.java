package com.example.fruitstore.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "baiviet")
@Data
public class articleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "maBaiViet", nullable = false)
    private String maBaiViet;

    @Column(name = "tieuDe", nullable = false)
    private String tieuDe;

    @Column(name = "noiDung")
    private String noiDung;

    @Column(name = "ngayDang", nullable = false)
    private java.sql.Date ngayDang;

    @Column(name = "nhanVienId")
    private Integer nhanVienId;

    @Column(name = "hinhAnh")
    private String hinhAnh;
}