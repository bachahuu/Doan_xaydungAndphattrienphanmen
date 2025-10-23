package com.example.fruitstore.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ProductDto {
    private Integer id;
    private String maSanPham; 
    private String tenSanPham;
    private String moTa;
    private String hinhAnh;
    private Double gia;
    private Integer soLuongTon; 
    private Double discountPercent;
    
    private String ngayNhap; 
    private String hanSuDung;

    private Integer danhMucId;
    private Integer nhaCungCapId;

    // Định dạng cho LocalDateTime/String (dùng cho input type="datetime-local")
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    // Phương thức tĩnh để chuyển đổi String từ DTO sang LocalDateTime cho Entity
    public static LocalDateTime convertToLocalDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }
        try {
            // Chuỗi từ input type="datetime-local" có format yyyy-MM-ddTHH:mm
            return LocalDateTime.parse(dateTimeString, FORMATTER);
        } catch (DateTimeParseException e) {
            System.err.println("Lỗi parse ngày tháng: " + e.getMessage());
            return null;
        }
    }

    // 1. Constructor mặc định
    public ProductDto() {}
    
    // 2. Constructor ánh xạ từ Entity SanPham 
    public ProductDto(com.example.fruitstore.entity.SanPham sanPham) {
        this.id = sanPham.getId(); 
        this.maSanPham = sanPham.getMaSanPham(); 
        this.tenSanPham = sanPham.getTenSanPham(); 
        this.moTa = sanPham.getMoTa(); 
        this.hinhAnh = sanPham.getHinhAnh(); 
        this.gia = sanPham.getGia();
        this.soLuongTon = sanPham.getSoLuongTon();
        if (sanPham.getKhuyenMai() != null && sanPham.getKhuyenMai().getGiaTri() != null) {
            this.discountPercent = sanPham.getKhuyenMai().getGiaTri();
        } else {
            this.discountPercent = 0.0;
        }
        
        // Ánh xạ ngày tháng (chuyển LocalDateTime sang String format cho input type="datetime-local")
        if (sanPham.getNgayNhap() != null) {
            this.ngayNhap = sanPham.getNgayNhap().format(FORMATTER);
        }
        if (sanPham.getHanSuDung() != null) {
            this.hanSuDung = sanPham.getHanSuDung().format(FORMATTER);
        }
        
        // Ánh xạ khóa ngoại
        if (sanPham.getDanhMuc() != null) {
            this.danhMucId = sanPham.getDanhMuc().getId();
        }
        if (sanPham.getNhaCungCap() != null) {
            this.nhaCungCapId = sanPham.getNhaCungCap().getId();
        }
    }


    // Constructor 5 tham số (Giữ nguyên)
    public ProductDto(Integer id, String tenSanPham, String moTa, String hinhAnh, Double gia) {
        this.id = id; 
        this.tenSanPham = tenSanPham; 
        this.moTa = moTa; 
        this.hinhAnh = hinhAnh; 
        this.gia = gia;
    }

    // ------------------------------------
    // GETTERS VÀ SETTERS (Giữ nguyên)
    // ------------------------------------
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getMaSanPham() { return maSanPham; }
    public void setMaSanPham(String maSanPham) { this.maSanPham = maSanPham; }
    
    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }
    
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    
    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }
    
    public Double getGia() { return gia; }
    public void setGia(Double gia) { this.gia = gia; }
    
    public Integer getSoLuongTon() { return soLuongTon; }
    public void setSoLuongTon(Integer soLuongTon) { this.soLuongTon = soLuongTon; }

    public Double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Double discountPercent) { this.discountPercent = discountPercent; }

    // NgayNhap và HanSuDung (String) 
    public String getNgayNhap() { return ngayNhap; }
    public void setNgayNhap(String ngayNhap) { this.ngayNhap = ngayNhap; }

    public String getHanSuDung() { return hanSuDung; }
    public void setHanSuDung(String hanSuDung) { this.hanSuDung = hanSuDung; }
    
    // Khóa ngoại
    public Integer getDanhMucId() { return danhMucId; }
    public void setDanhMucId(Integer danhMucId) { this.danhMucId = danhMucId; }

    public Integer getNhaCungCapId() { return nhaCungCapId; }
    public void setNhaCungCapId(Integer nhaCungCapId) { this.nhaCungCapId = nhaCungCapId; }
}