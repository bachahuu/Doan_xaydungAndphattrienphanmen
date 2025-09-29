package com.example.fruitstore.controller.admin;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.fruitstore.entity.EmployeeEntity;
import com.example.fruitstore.service.EmployeeService;

@Controller
@RequestMapping("/admin/employee")
public class adminEmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // =======================
    // 1️⃣ Trang quản lý nhân viên (Thymeleaf)
    // =======================
    @GetMapping("/manage")
    public String showManageEmployeePage(Model model) {
        // Truyền danh sách nhân viên nếu muốn render server-side
        List<EmployeeEntity> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "admin/products/manage_employee";
    }

    // =======================
    // 2️⃣ API JSON cho AJAX
    // =======================

    // Lấy danh sách nhân viên
    @GetMapping("/api/list")
    @ResponseBody
    public List<EmployeeEntity> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // Lấy nhân viên theo mã
    @GetMapping("/api/get/{maNhanVien}")
    @ResponseBody
    public ResponseEntity<EmployeeEntity> getEmployeeByMaNV(@PathVariable String maNhanVien) {
        try {
            EmployeeEntity employee = employeeService.getEmployeeByMaNV(maNhanVien);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Thêm nhân viên mới
    @PostMapping("/api/add")
    @ResponseBody
    public ResponseEntity<?> addEmployee(@RequestBody EmployeeEntity employee) {
        try {
            EmployeeEntity newEmployee = employeeService.addEmployee(employee);
            return ResponseEntity.ok(newEmployee);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cập nhật nhân viên
    @PutMapping("/api/update/{maNhanVien}")
    @ResponseBody
    public ResponseEntity<?> updateEmployee(@PathVariable String maNhanVien,
                                            @RequestBody EmployeeEntity employee) {
        try {
            EmployeeEntity updatedEmployee = employeeService.updateEmployee(maNhanVien, employee);
            return ResponseEntity.ok(updatedEmployee);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Xóa nhân viên
    @DeleteMapping("/api/delete/{maNhanVien}")
    @ResponseBody
    public ResponseEntity<?> deleteEmployee(@PathVariable String maNhanVien) {
        try {
            employeeService.deleteEmployee(maNhanVien);
            return ResponseEntity.ok("Xóa nhân viên thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
