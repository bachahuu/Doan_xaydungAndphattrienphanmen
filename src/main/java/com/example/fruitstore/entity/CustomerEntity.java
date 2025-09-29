package com.example.fruitstore.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "khachhang")
@Data
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "maKhachHang")
    private String maKhachHang;

    @Column(name = "tenKhachHang")
    private String tenKhachHang;

    @Column(name = "diaChi")
    private String diaChi;

    @Column(name = "soDienThoai")
    private String soDienThoai;

    @Column(name = "email")
    private String email;

    @Column(name = "loaiKhachHang")
    private String loaiKhachHang;

    @Column(name = "taiKhoanId")
    private Integer taiKhoanId;

    @Column(name = "trangThaiTaiKhoan")
    private Integer trangThaiTaiKhoan;
}
