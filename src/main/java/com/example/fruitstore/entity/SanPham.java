package com.example.fruitstore.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sanpham")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "discount" })
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "maSanPham")
    private String maSanPham;

    @Column(name = "tenSanPham")
    private String tenSanPham;

    @Column(name = "gia")
    private Double gia;

    @Column(name = "moTa")
    private String moTa;

    @Column(name = "hinhAnh")
    private String hinhAnh;

    @Column(name = "soLuongTon")
    private Integer soLuongTon;

    @Column(name = "ngayNhap")
    private LocalDateTime ngayNhap;

    @Column(name = "hanSuDung")
    private LocalDateTime hanSuDung;

    // --- Mối quan hệ với Nhà Cung Cấp ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nhaCungCapId")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private supplierEntity nhaCungCap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "danhMucId")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private DanhMuc danhMuc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khuyenmaiId")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private discountEntity khuyenMai;

    public SanPham() {
        this.ngayNhap = LocalDateTime.now();
    }

    // ==========================================================
    // BỔ SUNG: GETTERS VÀ SETTERS ĐẦY ĐỦ CHO TẤT CẢ CÁC THUỘC TÍNH
    // ==========================================================
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    // SỬA LỖI: Bổ sung getter/setter cho tenSanPham
    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    // SỬA LỖI: Bổ sung getter/setter cho gia
    public Double getGia() {
        return gia;
    }

    public void setGia(Double gia) {
        this.gia = gia;
    }

    // SỬA LỖI: Bổ sung getter/setter cho moTa
    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    // SỬA LỖI: Bổ sung getter/setter cho hinhAnh
    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    // SỬA LỖI: Bổ sung getter/setter cho soLuongTon
    public Integer getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(Integer soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    // SỬA LỖI: Bổ sung getter/setter cho ngayNhap
    public LocalDateTime getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDateTime ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    // SỬA LỖI: Bổ sung getter/setter cho hanSuDung
    public LocalDateTime getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(LocalDateTime hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

    // Getters/Setters cho mối quan hệ
    public supplierEntity getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(supplierEntity nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public DanhMuc getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(DanhMuc danhMuc) {
        this.danhMuc = danhMuc;
    }

    public discountEntity getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(discountEntity khuyenMai) {
        this.khuyenMai = khuyenMai;
    }
}