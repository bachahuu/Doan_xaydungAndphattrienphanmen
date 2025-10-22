package com.example.fruitstore.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.fruitstore.entity.CustomerEntity;
import com.example.fruitstore.entity.EmployeeEntity; // Lớp entity cho nhân viên
import com.example.fruitstore.entity.supplierEntity; // Lớp entity cho nhà cung cấp
import com.example.fruitstore.service.supplierService; // Dịch vụ cho nhà cung cấp
import com.example.fruitstore.service.order.orderService;
import com.example.fruitstore.service.CustomerService;
import com.example.fruitstore.service.EmployeeService; // Dịch vụ cho nhân viên
import com.example.fruitstore.service.phuongThucThanhToanService;
import com.example.fruitstore.entity.order.orderEntity;

@Controller
public class adminDashboard {
    @Autowired
    private orderService orderService;

    @GetMapping("/admin/orders")
    public String showorder(@RequestParam(value = "keyword", required = false) String keyword,
            Model model) {
        List<orderEntity> orders = orderService.searchOrders(keyword);
        model.addAttribute("keyword", keyword); // THÊM keyword vào model để giữ giá trị trên ô tìm kiếm
        model.addAttribute("orders", orders);
        model.addAttribute("view", "admin/products/manage_order");
        return "admin/layout/main";
    }

    // tìm

    @GetMapping("/admin/product")
    public String showproduct(Model model) {
        model.addAttribute("view", "admin/products/manage_product");
        return "admin/layout/main";
    }

    @Autowired
    private supplierService supplierService;

    @GetMapping("/admin/supplier")
    public String showSupplierList(Model model) {
        List<supplierEntity> suppliers = supplierService.getAllSuppliers();
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("view", "admin/products/manage_supplier");
        return "admin/layout/main";
    }

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/admin/employee")
    public String showEmployeeList(Model model) {
        List<EmployeeEntity> employees = employeeService.getAllEmployees(); // Lấy danh sách nhân viên
        model.addAttribute("employees", employees);
        model.addAttribute("view", "admin/products/manage_employee");
        return "admin/layout/main";
    }

    @Autowired
    private CustomerService customerService;

    @GetMapping("/admin/customer")
    public String showCustomerList(Model model) {
        // Nếu chỉ xem danh sách thì gọi service lấy tất cả khách hàng
        List<CustomerEntity> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        // Truyền tên view để main.html load fragment manage_customer
        model.addAttribute("view", "admin/products/manage_customer");
        return "admin/layout/main";
    }
}
