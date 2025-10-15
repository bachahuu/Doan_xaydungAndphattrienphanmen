package com.example.fruitstore.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class OrderDetailDTO {
    private String maDonHang;
    private String trangThai;
    private Date ngayTao;
    private String tenNguoiNhan;
    private String soDienThoaiNguoiNhan;
    private String diaChiGiaoHang;
    private BigDecimal tongTien;
    private String khachHangTen;
    private String khachHangEmail;
    private String khachHangSoDienThoai;
    private String phuongThucName;
    private String discountName;
    private List<OrderItemDTO> items;
}
