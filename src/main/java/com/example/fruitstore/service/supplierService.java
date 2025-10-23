package com.example.fruitstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.fruitstore.respository.suppilerRespository;
import com.example.fruitstore.entity.supplierEntity;
import java.util.List;

@Service
public class supplierService {
    @Autowired
    private suppilerRespository suppilerRespository;

    public List<supplierEntity> getAllSuppliers() {
        return suppilerRespository.findAll();
    }

    public supplierEntity getSupplierById(Integer id) { // <-- THÊM MỚI
        return suppilerRespository.findById(id).orElse(null);
    }

    public supplierEntity getSupplierByMaNCC(String maNCC) {
        return suppilerRespository.findByMaNCC(maNCC)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp với mã: " + maNCC));
    }

    public supplierEntity updateSupplier(String maNCC, supplierEntity supplierUpdate) {
        supplierEntity supplier = getSupplierByMaNCC(maNCC);

        supplier.setTenNCC(supplierUpdate.getTenNCC());
        supplier.setSoDienThoai(supplierUpdate.getSoDienThoai());
        supplier.setEmail(supplierUpdate.getEmail());
        supplier.setDiaChi(supplierUpdate.getDiaChi());

        return suppilerRespository.save(supplier);
    }

    public void deleteSupplier(String maNCC) {
        supplierEntity supplier = getSupplierByMaNCC(maNCC);
        suppilerRespository.delete(supplier);
    }

    public supplierEntity addSupplier(supplierEntity supplier) {
        // Kiểm tra xem mã NCC đã tồn tại chưa
        if (suppilerRespository.findByMaNCC(supplier.getMaNCC()).isPresent()) {
            throw new RuntimeException("Mã nhà cung cấp đã tồn tại!");
        }
        return suppilerRespository.save(supplier);
    }
}
