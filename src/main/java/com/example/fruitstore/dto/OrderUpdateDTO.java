package com.example.fruitstore.dto;

import lombok.Data;

@Data
public class OrderUpdateDTO {
    private String trangThai;
    private String diaChiGiaoHang;
    private String ghiChu;
    private Integer phuongThucThanhToanId;
    private Integer khuyenMaiId;
}
