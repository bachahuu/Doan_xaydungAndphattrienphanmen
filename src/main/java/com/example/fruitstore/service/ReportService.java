package com.example.fruitstore.service;

import com.example.fruitstore.entity.ReportEntity;
import com.example.fruitstore.respository.OrderRepository;
import com.example.fruitstore.respository.ProductRepository;
import com.example.fruitstore.respository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;

@Service
public class ReportService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReportRepository reportRepository;

    // =================== 💰 DOANH THU ===================
    public Double getRevenueByDay(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        Double total = orderRepository.sumTotalByDate(startOfDay, endOfDay);
        return total != null ? total : 0.0;
    }

    public Double getRevenueByMonth(int year, int month) {
        BigDecimal total = orderRepository.sumTotalByMonth(year, month);
        return total != null ? total.doubleValue() : 0.0;
    }

    public Double getRevenueByYear(int year) {
        BigDecimal total = orderRepository.sumTotalByYear(year);
        return total != null ? total.doubleValue() : 0.0;
    }

    public Double getRevenueByRange(LocalDate from, LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(23, 59, 59);
        Double total = orderRepository.sumTotalByRange(start, end);
        return total != null ? total : 0.0;
    }

    // =================== 🛒 SẢN PHẨM BÁN ĐƯỢC ===================
    public List<Map<String, Object>> getSoldProducts(String mode, LocalDate date, Integer month, Integer year) {
        LocalDate from;
        LocalDate to;

        switch (mode) {
            case "day" -> {
                from = date;
                to = date;
            }
            case "month" -> {
                from = LocalDate.of(year, month, 1);
                to = from.plusMonths(1).minusDays(1);
            }
            case "year" -> {
                from = LocalDate.of(year, 1, 1);
                to = from.plusYears(1).minusDays(1);
            }
            default -> throw new IllegalArgumentException("Chế độ không hợp lệ: " + mode);
        }

        LocalDateTime startRange = from.atStartOfDay();
        LocalDateTime endRange = to.atTime(23, 59, 59);

        List<Object[]> results = orderRepository.getSoldProductsBetween(
                startRange,
                endRange);
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("tenSanPham", row[0]);
            map.put("soLuong", ((Number) row[1]).intValue());
            map.put("donGia", ((Number) row[2]).doubleValue());
            map.put("tongTien", ((Number) row[3]).doubleValue());
            list.add(map);
        }
        return list;
    }

    // =================== 📦 ĐƠN HÀNG ===================
    public Integer getOrderCountByDay(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        Integer count = orderRepository.countByDate(startOfDay, endOfDay);
        return count != null ? count : 0;
    }

    public Integer getOrderCountByMonth(int year, int month) {
        Integer count = orderRepository.countByMonth(year, month);
        return count != null ? count : 0;
    }

    public Integer getOrderCountByYear(int year) {
        Integer count = orderRepository.countByYear(year);
        return count != null ? count : 0;
    }

    // =================== 🏷️ TỒN KHO ===================
    public List<Object[]> getInventory() {
        return productRepository.getInventory();
    }

    // =================== 🧾 TẠO BÁO CÁO ===================
    public ReportEntity createReport(ReportEntity reportData) {
        LocalDate today = LocalDate.now();

        // Dùng dữ liệu người nhập
        ReportEntity report = ReportEntity.builder()
                .loaiBaoCao(reportData.getLoaiBaoCao())
                .nguoiLap(reportData.getNguoiLap())
                .ghiChu(reportData.getGhiChu())
                .ngayTao(today)
                .thoiGianBaoCao(today.toString())
                .tongDoanhThu(reportData.getTongDoanhThu())
                .tongDonHang(reportData.getTongDonHang())
                .tongSanPhamTon(reportData.getTongSanPhamTon())
                .build();

        return reportRepository.save(report);
    }

    // =================== 📋 DANH SÁCH BÁO CÁO ===================
    public List<ReportEntity> getAllReports() {
        return reportRepository.findAll();
    }

    // =================== ✏️ CẬP NHẬT BÁO CÁO ===================
    public ReportEntity updateReport(Long id, ReportEntity updated) {
        return reportRepository.findById(id)
                .map(report -> {
                    report.setLoaiBaoCao(updated.getLoaiBaoCao());
                    report.setNguoiLap(updated.getNguoiLap());
                    report.setGhiChu(updated.getGhiChu());
                    report.setTongDoanhThu(updated.getTongDoanhThu());
                    report.setTongDonHang(updated.getTongDonHang());
                    report.setTongSanPhamTon(updated.getTongSanPhamTon());
                    return reportRepository.save(report);
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy báo cáo có ID: " + id));
    }

    // =================== ❌ XOÁ BÁO CÁO ===================
    public void deleteReport(Long id) {
        if (!reportRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy báo cáo để xoá!");
        }
        reportRepository.deleteById(id);
    }
}
