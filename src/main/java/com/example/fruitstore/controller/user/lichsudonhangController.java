package com.example.fruitstore.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.fruitstore.entity.CustomerEntity;
import com.example.fruitstore.entity.loginCustomerEntity;
import com.example.fruitstore.entity.order.orderEntity;
import com.example.fruitstore.service.order.orderService;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

@Controller
public class lichsudonhangController {

    @Autowired
    private orderService orderService;

    @GetMapping("/order-history")
    public ModelAndView showOrderHistoryPage(HttpSession session) {
        loginCustomerEntity loginCustomer = (loginCustomerEntity) session.getAttribute("customer");

        if (loginCustomer == null) {
            // Nếu chưa đăng nhập thì chuyển hướng sang trang login
            return new ModelAndView("redirect:/login");
        }

        CustomerEntity customer = loginCustomer.getCustomer();
        if (customer == null) {
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("message", "Lỗi: Dữ liệu khách hàng không hợp lệ.");
            return mav;
        }

        Integer khachHangId = customer.getId();

        // Lấy danh sách đơn hàng của khách
        List<orderEntity> orders = orderService.find(khachHangId);

        // 2. Trả về view chính và chỉ định fragment content là trang order-history
        ModelAndView mav = new ModelAndView("user/layout/main");
        mav.addObject("orders", orders);
        mav.addObject("view", "user/products/lichsudonhang"); // Đường dẫn tới file order-history.html
        return mav;
    }

    @GetMapping("/order-history/{id}")
    public String showOrderDetail(@PathVariable("id") Integer orderId, Model model, HttpSession session) {
        loginCustomerEntity loginCustomer = (loginCustomerEntity) session.getAttribute("customer");
        if (loginCustomer == null) {
            return "redirect:/login";
        }

        CustomerEntity customer = loginCustomer.getCustomer();

        try {
            orderEntity order = orderService.getOrderById(orderId);

            if (!order.getKhachHang().getId().equals(customer.getId())) {
                return "redirect:/order-history";
            }

            model.addAttribute("order", order);
            model.addAttribute("view", "user/products/order-detail");
            return "user/layout/main";
        } catch (Exception e) {
            e.printStackTrace(); // giúp debug khi lỗi render
            return "redirect:/order-history";
        }
    }

    @PostMapping("/order-history/{id}/cancel")
    public String cancelOrder(@PathVariable("id") Integer orderId, HttpSession session) {
        loginCustomerEntity loginCustomer = (loginCustomerEntity) session.getAttribute("customer");
        if (loginCustomer == null) {
            return "redirect:/login";
        }

        try {
            // Gọi service để hủy, truyền vào ID khách hàng để bảo mật
            orderService.cancelOrder(orderId, loginCustomer.getCustomer().getId());
        } catch (Exception e) {
            System.err.println("Lỗi hủy đơn hàng: " + e.getMessage());
        }

        // Sau khi hủy, chuyển hướng về trang danh sách lịch sử
        return "redirect:/order-history";
    }

}
