package com.example.fruitstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "khachhang")
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
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

    @OneToOne
    @JoinColumn(name = "taiKhoanId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @JsonIgnore
    private loginCustomerEntity account;

    @Column(name = "trangThaiTaiKhoan")
    private Integer trangThaiTaiKhoan;
}
