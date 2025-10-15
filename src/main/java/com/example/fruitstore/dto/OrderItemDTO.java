package com.example.fruitstore.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Integer sanPhamId;
    private String tenSanPham;
    private Integer soLuong;
    private BigDecimal gia;
}
