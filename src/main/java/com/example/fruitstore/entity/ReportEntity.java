package com.example.fruitstore.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "baocao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaBaoCao")
    private Long maBaoCao;

    @Column(name = "NgayTao")
    private LocalDate ngayTao;

    @Column(name = "LoaiBaoCao")
    private String loaiBaoCao;

    @Column(name = "ThoiGianBaoCao")
    private String thoiGianBaoCao;

    @Column(name = "TongDoanhThu")
    private Double tongDoanhThu;

    @Column(name = "TongDonHang")
    private Integer tongDonHang;

    @Column(name = "TongSanPhamTon")
    private Integer tongSanPhamTon;

    @Column(name = "NguoiLap")
    private String nguoiLap;

    @Column(name = "GhiChu")
    private String ghiChu;
}
