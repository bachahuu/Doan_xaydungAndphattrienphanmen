package com.example.fruitstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "danhmuc")
public class DanhMuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Map to the actual database column name `danhMucId` (matches your DB)
    @Column(name = "danhMucId")
    private String maDanhMuc;

    @Column(name = "tenDanhMuc")
    private String tenDanhMuc;

    @Column(name = "moTa")
    private String moTa;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    // Getter/Setter cho maDanhMuc (mã danh mục)
    public String getMaDanhMuc() { return maDanhMuc; }
    public void setMaDanhMuc(String maDanhMuc) { this.maDanhMuc = maDanhMuc; }
    
    public String getTenDanhMuc() { return tenDanhMuc; }
    public void setTenDanhMuc(String tenDanhMuc) { this.tenDanhMuc = tenDanhMuc; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
}