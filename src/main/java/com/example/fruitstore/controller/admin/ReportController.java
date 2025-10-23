package com.example.fruitstore.controller.admin;

import com.example.fruitstore.entity.ReportEntity;
import com.example.fruitstore.respository.OrderRepository;
import com.example.fruitstore.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private OrderRepository orderRepository;

    // =================== 💰 DOANH THU ===================
    @GetMapping("/revenue/day")
    public Double getRevenueByDay(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return reportService.getRevenueByDay(date);
    }

    @GetMapping("/revenue/month")
    public Double getRevenueByMonth(@RequestParam int year, @RequestParam int month) {
        return reportService.getRevenueByMonth(year, month);
    }

    @GetMapping("/revenue/year")
    public Double getRevenueByYear(@RequestParam int year) {
        return reportService.getRevenueByYear(year);
    }

    // =================== 💰 DOANH THU + SẢN PHẨM BÁN ĐƯỢC ===================
    @GetMapping("/revenue-with-products/{mode}")
    public Map<String, Object> getRevenueWithProducts(
            @PathVariable String mode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        Map<String, Object> response = new HashMap<>();
        double totalRevenue = 0.0;
        List<Map<String, Object>> soldProducts = Collections.emptyList();

        try {
            if ("day".equals(mode)) {
                totalRevenue = reportService.getRevenueByDay(date);
            } else if ("month".equals(mode)) {
                totalRevenue = reportService.getRevenueByMonth(year, month);
            } else if ("year".equals(mode)) {
                totalRevenue = reportService.getRevenueByYear(year);
            }

            soldProducts = reportService.getSoldProducts(mode, date, month, year);

            response.put("totalRevenue", totalRevenue);
            response.put("products", soldProducts);
        } catch (Exception e) {
            System.err.println("❌ LỖI KHI XỬ LÝ DOANH THU:");
            e.printStackTrace(); // ✅ In lỗi chi tiết ra console
            response.put("error", e.getMessage());
        }

        return response;
    }

    // =================== 📦 ĐƠN HÀNG ===================
    @GetMapping("/orders/day")
    public Integer getOrderCountByDay(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return reportService.getOrderCountByDay(date);
    }

    @GetMapping("/orders/month")
    public Integer getOrderCountByMonth(@RequestParam int year, @RequestParam int month) {
        return reportService.getOrderCountByMonth(year, month);
    }

    @GetMapping("/orders/year")
    public Integer getOrderCountByYear(@RequestParam int year) {
        return reportService.getOrderCountByYear(year);
    }

    // =================== 🏷️ TỒN KHO ===================
    @GetMapping("/inventory")
    public List<Object[]> getInventory() {
        return reportService.getInventory();
    }

    // =================== ⬇️ XUẤT EXCEL ===================
    @GetMapping("/export/excel")
    public void exportReportExcel(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Báo cáo");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Ngày tạo báo cáo");
        header.createCell(1).setCellValue("Tổng doanh thu (VNĐ)");
        header.createCell(2).setCellValue("Tổng đơn hàng");

        LocalDate now = LocalDate.now();
        Double revenue = reportService.getRevenueByDay(now);
        Integer orders = reportService.getOrderCountByDay(now);

        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(now.toString());
        row.createCell(1).setCellValue(revenue);
        row.createCell(2).setCellValue(orders);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=baocao.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    // =================== 🧾 QUẢN LÝ BÁO CÁO ===================
    @PostMapping("/create")
    public ReportEntity createReport(@RequestBody ReportEntity report) {
        return reportService.createReport(report);
    }

    @GetMapping("/list")
    public List<ReportEntity> getAllReports() {
        return reportService.getAllReports();
    }

    // =================== 📝 XUẤT WORD ===================
    @GetMapping("/export/word")
    public void exportReportWord(HttpServletResponse response) throws IOException {
        List<ReportEntity> reports = reportService.getAllReports();

        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=baocao.docx");

        try (org.apache.poi.xwpf.usermodel.XWPFDocument doc = new org.apache.poi.xwpf.usermodel.XWPFDocument()) {
            org.apache.poi.xwpf.usermodel.XWPFParagraph title = doc.createParagraph();
            org.apache.poi.xwpf.usermodel.XWPFRun run = title.createRun();
            run.setText("Danh sách báo cáo");
            run.setBold(true);
            run.setFontSize(16);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (ReportEntity r : reports) {
                XWPFParagraph p = doc.createParagraph();
                XWPFRun text = p.createRun();
                String ngay = r.getNgayTao() != null ? r.getNgayTao().format(fmt) : "";
                text.setText("• " + r.getLoaiBaoCao() + " - " + r.getNguoiLap() +
                        " (" + ngay + ")" +
                        (r.getGhiChu() != null ? " — " + r.getGhiChu() : ""));
            }
            doc.write(response.getOutputStream());
        }
    }

    // =================== ✏️ CẬP NHẬT BÁO CÁO ===================
    @PutMapping("/update/{id}")
    public ReportEntity updateReport(@PathVariable Long id, @RequestBody ReportEntity updatedReport) {
        return reportService.updateReport(id, updatedReport);
    }

    // =================== ❌ XOÁ BÁO CÁO ===================
    @DeleteMapping("/delete/{id}")
    public void deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
    }
}
