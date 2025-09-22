package com.example.fruitstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "nhacungcap")
@Data

public class supplierEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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

}
