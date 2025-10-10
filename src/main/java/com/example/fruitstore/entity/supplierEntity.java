package com.example.fruitstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "nhacungcap")
@Data

public class supplierEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nhaCungCapId")
    private Integer id;
    @Column(name = "maNCC")
    private String maNCC;
    @Column(name = "tenNCC")
    private String tenNCC;
    @Column(name = "soDienThoai")
    private String soDienThoai;
    @Column(name = "email")
    private String email;
    @Column(name = "diaChi")
    private String diaChi;

    public String getTenNCC() {
        return tenNCC;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }
}
