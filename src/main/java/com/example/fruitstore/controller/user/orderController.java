package com.example.fruitstore.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.fruitstore.entity.CustomerEntity;
import com.example.fruitstore.entity.loginCustomerEntity;
import com.example.fruitstore.service.order.orderService;
import java.util.Collections;
import jakarta.servlet.http.HttpSession;

@Controller
public class orderController {
    @Autowired
    private orderService orderService;

    @PostMapping("/orders/checkout")
    @ResponseBody
    public ResponseEntity<?> checkout(@RequestParam Map<String, String> params, HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<String, String> shippingInfo = (Map<String, String>) session.getAttribute("shippingInfo");
        if (shippingInfo == null) {
            return new ResponseEntity<>(
                    Collections.singletonMap("message", "Phiên làm việc hết hạn, vui lòng thực hiện lại."),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            String orderNote = params.get("orderNote");
            if (orderNote != null) {
                shippingInfo.put("orderNote", orderNote);
            }
            // Lấy paymentMethodId từ request params
            String paymentMethodStr = params.get("paymentMethod");
            if (paymentMethodStr == null || paymentMethodStr.isEmpty()) {
                return new ResponseEntity<>(
                        Collections.singletonMap("message", "Vui lòng chọn phương thức thanh toán."),
                        HttpStatus.BAD_REQUEST);
            }
            Integer paymentMethodId = Integer.valueOf(paymentMethodStr);

            // Lấy discountId từ request params
            Integer discountId = null;
            String discountIdStr = params.get("discountId");
            if (discountIdStr != null && !discountIdStr.isEmpty()) {
                discountId = Integer.valueOf(discountIdStr);
            }

            loginCustomerEntity loginCustomer = (loginCustomerEntity) session.getAttribute("customer");
            // kiểm tra đăng nhập
            if (loginCustomer == null) {
                return new ResponseEntity<>(
                        Collections.singletonMap("message", "Vui lòng đăng nhập để đặt hàng."),
                        HttpStatus.UNAUTHORIZED);
            }
            CustomerEntity customer = loginCustomer.getCustomer();
            if (customer == null) {
                return new ResponseEntity<>("Lỗi: Dữ liệu khách hàng không hợp lệ.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Integer userId = customer.getId();

            // GỌI SERVICE ĐỂ THỰC HIỆN TOÀN BỘ NGHIỆP VỤ
            orderService.createOrder(userId, shippingInfo, paymentMethodId, discountId, session);

            // Nếu không có lỗi, trả về thành công
            return new ResponseEntity<>(Collections.singletonMap("message", "Đặt hàng thành công!"), HttpStatus.OK);

        } catch (IllegalStateException e) {
            // Bắt các lỗi nghiệp vụ
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Bắt các lỗi chung khác
            e.printStackTrace();
            return new ResponseEntity<>(
                    Collections.singletonMap("message", "Đã có lỗi xảy ra trong quá trình xử lý đơn hàng."),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
