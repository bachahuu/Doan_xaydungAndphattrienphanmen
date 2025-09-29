package com.example.fruitstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sanpham")
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

        @ManyToOne
        @JoinColumn(name = "nhaCungCapId")
        private NhaCungCap nhaCungCap;

        // Getter & Setter
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getMaSanPham() { return maSanPham; }
        public void setMaSanPham(String maSanPham) { this.maSanPham = maSanPham; }
        public String getTenSanPham() { return tenSanPham; }
        public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }
        public Double getGia() { return gia; }
        public void setGia(Double gia) { this.gia = gia; }
        public String getMoTa() { return moTa; }
        public void setMoTa(String moTa) { this.moTa = moTa; }
        public String getHinhAnh() { return hinhAnh; }
        public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }
        public Integer getSoLuongTon() { return soLuongTon; }
        public void setSoLuongTon(Integer soLuongTon) { this.soLuongTon = soLuongTon; }
        public NhaCungCap getNhaCungCap() { return nhaCungCap; }
        public void setNhaCungCap(NhaCungCap nhaCungCap) { this.nhaCungCap = nhaCungCap; }
}