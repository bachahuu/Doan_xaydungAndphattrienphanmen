package com.example.fruitstore.entity;

import java.util.List;

import com.example.fruitstore.entity.order.orderEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "thanhtoan")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "orders" })
public class phuongThucThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tenPTTT")
    private String name;

    @Column(name = "moTa")
    private String description;

    @Column(name = "trangThai")
    private int trangThai;

    // Quan hệ ngược lại với Order
    @OneToMany(mappedBy = "phuongThucThanhToan", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private List<orderEntity> orders;
}
