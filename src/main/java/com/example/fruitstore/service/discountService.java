package com.example.fruitstore.service;

import com.example.fruitstore.entity.discountEntity;
import com.example.fruitstore.respository.discountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class discountService {
    @Autowired
    private discountRepository discountRepository;

    public List<discountEntity> getAllDiscounts() {
        // **TỰ ĐỘNG CẬP NHẬT TRẠNG THÁI TRƯỚC KHI LẤY DANH SÁCH**
        updateAllDiscountStatuses();
        return discountRepository.findAll();
    }

    public Optional<discountEntity> getDiscountById(Integer id) {
        discountEntity discount = discountRepository.findById(id).orElse(null);
        if (discount != null) {
            // **TỰ ĐỘNG CẬP NHẬT TRẠNG THÁI CHO ĐƠN LẺ**
            updateDiscountStatus(discount);
            discountRepository.save(discount);
        }
        return Optional.ofNullable(discount);
    }

    // **PHƯƠNG THỨC TỰ ĐỘNG CẬP NHẬT TRẠNG THÁI CHO TẤT CẢ**
    @Scheduled(fixedRate = 60000) // Chạy mỗi 60 giây
    public void updateAllDiscountStatuses() {
        List<discountEntity> allDiscounts = discountRepository.findAll();
        Date today = new Date();
        
        for (discountEntity discount : allDiscounts) {
            updateDiscountStatus(discount);
            discountRepository.save(discount);
        }
    }

    // **PHƯƠNG THỨC CẬP NHẬT TRẠNG THÁI CHO 1 KHUYẾN MÃI**
    private void updateDiscountStatus(discountEntity discount) {
        Date today = new Date();
        
        // Chuyển đổi Date thành java.sql.Date để so sánh
        java.sql.Date todaySql = new java.sql.Date(today.getTime());
        java.sql.Date startDate = discount.getNgayBatDau();
        java.sql.Date endDate = discount.getNgayKetThuc();
        
        // **QUY TẮC CẬP NHẬT TỰ ĐỘNG:**
        if (todaySql.before(startDate)) {
            // Chưa đến ngày bắt đầu → Chưa áp dụng (2)
            discount.setTrangThai(2);
        } else if (todaySql.after(endDate)) {
            // Qua ngày kết thúc → Hết hiệu lực (0)
            discount.setTrangThai(0);
        } else {
            // Trong khoảng thời gian → Đang áp dụng (1)
            discount.setTrangThai(1);
        }
    }

    public discountEntity addDiscount(discountEntity discount) {
        // Kiểm tra mã khuyến mãi đã tồn tại
        if (discountRepository.existsByMaKM(discount.getMaKM())) {
            throw new RuntimeException("Mã khuyến mãi đã tồn tại!");
        }
        
        // Validation giá trị đơn hàng tối thiểu
        if (discount.getGiaTriDonHangToiThieu() == null) {
            discount.setGiaTriDonHangToiThieu(0.0);
        }
        if (discount.getGiaTriDonHangToiThieu() < 0) {
            throw new RuntimeException("Giá trị đơn hàng tối thiểu không được âm!");
        }
        if (discount.getGiaTriDonHangToiThieu() > 99999999.99) {
            throw new RuntimeException("Giá trị đơn hàng tối thiểu không được vượt quá 99,999,999.99!");
        }
        
        // **TỰ ĐỘNG ĐẶT TRẠNG THÁI THEO NGÀY**
        updateDiscountStatus(discount);
        
        // Ngày hiện tại
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        if (discount.getNgayBatDau().before(today)) {
            throw new RuntimeException("Ngày bắt đầu không được trước ngày hiện tại!");
        }
        if (discount.getNgayKetThuc().before(today)) {
            throw new RuntimeException("Ngày kết thúc không được trước ngày hiện tại!");
        }
        if (discount.getNgayKetThuc().before(discount.getNgayBatDau())) {
            throw new RuntimeException("Ngày kết thúc không được trước ngày bắt đầu!");
        }
        
        return discountRepository.save(discount);
    }

    public discountEntity updateDiscount(Integer id, discountEntity update) {
        discountEntity discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mã khuyến mãi với id: " + id));

        // Kiểm tra mã không trùng (trừ chính nó)
        if (!discount.getMaKM().equals(update.getMaKM()) && discountRepository.existsByMaKM(update.getMaKM())) {
            throw new RuntimeException("Mã khuyến mãi đã tồn tại!");
        }
        
        // Giá trị đơn hàng tối thiểu
        if (update.getGiaTriDonHangToiThieu() == null) {
            update.setGiaTriDonHangToiThieu(0.0);
        }
        if (update.getGiaTriDonHangToiThieu() < 0) {
            throw new RuntimeException("Giá trị đơn hàng tối thiểu không được âm!");
        }
        if (update.getGiaTriDonHangToiThieu() > 99999999.99) {
            throw new RuntimeException("Giá trị đơn hàng tối thiểu không được vượt quá 99,999,999.99!");
        }
        
        // **TỰ ĐỘNG CẬP NHẬT TRẠNG THÁI THEO NGÀY MỚI**
        update.setNgayBatDau(update.getNgayBatDau());
        update.setNgayKetThuc(update.getNgayKetThuc());
        updateDiscountStatus(update);
        
        // Ngày hiện tại
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        // if (update.getNgayBatDau().before(today)) {
        //     throw new RuntimeException("Ngày bắt đầu không được trước ngày hiện tại!");
        // }
        // if (update.getNgayKetThuc().before(today)) {
        //     throw new RuntimeException("Ngày kết thúc không được trước ngày hiện tại!");
        // }
        if (update.getNgayKetThuc().before(update.getNgayBatDau())) {
            throw new RuntimeException("Ngày kết thúc không được trước ngày bắt đầu!");
        }

        // Cập nhật thông tin
        discount.setMaKM(update.getMaKM());
        discount.setTenKM(update.getTenKM());
        discount.setGiaTri(update.getGiaTri());
        discount.setGiaTriDonHangToiThieu(update.getGiaTriDonHangToiThieu());
        discount.setNgayBatDau(update.getNgayBatDau());
        discount.setNgayKetThuc(update.getNgayKetThuc());
        discount.setTrangThai(update.getTrangThai());

        return discountRepository.save(discount);
    }

    public void deleteDiscount(Integer id) {
        if (!discountRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy mã khuyến mãi với id: " + id);
        }
        discountRepository.deleteById(id);
    }
}