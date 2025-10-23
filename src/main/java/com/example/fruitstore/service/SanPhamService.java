package com.example.fruitstore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fruitstore.entity.SanPham;
import com.example.fruitstore.respository.SanPhamRepository;
import java.util.Collections;

@Service
public class SanPhamService {
    @Autowired
    private SanPhamRepository sanPhamRepository;

    public List<SanPham> getAllSanPham() {
        return sanPhamRepository.findAll();
    }

    public SanPham getSanPhamById(Integer id) {
        return sanPhamRepository.findById(id).orElse(null);
    }

    public List<SanPham> searchSanPhamByName(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return sanPhamRepository.findByTenSanPhamContainingIgnoreCase(query)
                .stream()
                .limit(5) // Giới hạn 5 kết quả gợi ý
                .collect(Collectors.toList());
    }

    public SanPham save(SanPham sp) {
        return sanPhamRepository.save(sp);
    }

    public void deleteById(Integer id) {
        sanPhamRepository.deleteById(id);
    }

    public List<SanPham> getSanPhamByDanhMucId(Integer danhMucId) {
        if (danhMucId == null)
            return getAllSanPham();
        return sanPhamRepository.findByDanhMuc_Id(danhMucId);
    }

    @Transactional
    public SanPham getSanPhamByIdWithDetails(Integer id) {
        return sanPhamRepository.findByIdWithDetails(id);
    }
}
