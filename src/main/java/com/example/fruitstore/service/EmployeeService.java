package com.example.fruitstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.fruitstore.respository.EmployeeRepository;
import com.example.fruitstore.entity.EmployeeEntity;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Lấy danh sách tất cả nhân viên
     */
    public List<EmployeeEntity> getAllEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * Lấy thông tin nhân viên theo mã nhân viên
     */
    public EmployeeEntity getEmployeeByMaNV(String maNhanVien) {
        return employeeRepository.findByMaNV(maNhanVien)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy nhân viên với mã: " + maNhanVien));
    }

    /**
     * Cập nhật thông tin nhân viên
     */
    public EmployeeEntity updateEmployee(String maNhanVien, EmployeeEntity employeeUpdate) {
        EmployeeEntity employee = getEmployeeByMaNV(maNhanVien);

        employee.setTenNV(employeeUpdate.getTenNV());
        employee.setSoDienThoai(employeeUpdate.getSoDienThoai());
        employee.setDiaChi(employeeUpdate.getDiaChi());
        employee.setChucVu(employeeUpdate.getChucVu());

        return employeeRepository.save(employee);
    }

    /**
     * Xóa nhân viên theo mã nhân viên
     */
    public void deleteEmployee(String maNhanVien) {
        EmployeeEntity employee = getEmployeeByMaNV(maNhanVien);
        employeeRepository.delete(employee);
    }

    /**
     * Thêm nhân viên mới
     */
    public EmployeeEntity addEmployee(EmployeeEntity employee) {
        // Kiểm tra mã nhân viên đã tồn tại chưa
        if (employeeRepository.findByMaNV(employee.getMaNV()).isPresent()) {
            throw new RuntimeException("Mã nhân viên đã tồn tại!");
        }
        return employeeRepository.save(employee);
    }
}
