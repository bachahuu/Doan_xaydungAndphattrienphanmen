package com.example.fruitstore.service;

import com.example.fruitstore.entity.discountEntity;
import com.example.fruitstore.respository.discountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class discountService {
    @Autowired
    private discountRepository discountRepository;

    public List<discountEntity> getAllDiscounts() {
        return discountRepository.findAll();
    }

    public Optional<discountEntity> getDiscountById(Integer id) {
        return discountRepository.findById(id);
    }

    public discountEntity updateDiscount(Integer id, discountEntity update) {
        discountEntity discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mã khuyến mãi với id: " + id));

        // Kiểm tra mã không trùng (trừ chính nó)
        if (!discount.getMaKM().equals(update.getMaKM()) && discountRepository.existsByMaKM(update.getMaKM())) {
            throw new RuntimeException("Mã khuyến mãi đã tồn tại!");
        }
        // Giá trị giảm giá
        if (update.getGiaTri() < 0 || update.getGiaTri() > 100) {
            throw new RuntimeException("Giá trị giảm giá chỉ được từ 0 đến 100!");
        }
        // Ngày hiện tại
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        if (update.getNgayBatDau().before(today)) {
            throw new RuntimeException("Ngày bắt đầu không được trước ngày hiện tại!");
        }
        if (update.getNgayKetThuc().before(today)) {
            throw new RuntimeException("Ngày kết thúc không được trước ngày hiện tại!");
        }
        if (update.getNgayKetThuc().before(update.getNgayBatDau())) {
            throw new RuntimeException("Ngày kết thúc không được trước ngày bắt đầu!");
        }

        // Cập nhật thông tin
        discount.setMaKM(update.getMaKM());
        discount.setTenKM(update.getTenKM());
        discount.setGiaTri(update.getGiaTri());
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
