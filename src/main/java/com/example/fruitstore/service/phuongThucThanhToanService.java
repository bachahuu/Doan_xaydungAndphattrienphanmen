package com.example.fruitstore.service;

import com.example.fruitstore.entity.phuongThucThanhToan;
import com.example.fruitstore.respository.phuongThucThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class phuongThucThanhToanService {
    @Autowired
    private phuongThucThanhToanRepository phuongThucThanhToanRepository;

    public List<phuongThucThanhToan> findAllActive() {
        return phuongThucThanhToanRepository.findByTrangThai(1);
    }

}
