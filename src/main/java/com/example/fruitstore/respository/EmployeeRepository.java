package com.example.fruitstore.respository;

import com.example.fruitstore.entity.EmployeeEntity; // Lớp entity cho nhân viên
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

    // Tìm nhân viên theo mã nhân viên (maNV)
    Optional<EmployeeEntity> findByMaNV(String maNhanVien);
}
