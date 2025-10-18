package com.example.fruitstore.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.fruitstore.service.order.orderService;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import com.example.fruitstore.entity.loginCustomerEntity;
import com.example.fruitstore.entity.order.orderEntity;
import com.example.fruitstore.service.order.moMoService;
import com.example.fruitstore.service.cartService;
import org.springframework.ui.Model;

@Controller
public class PaymentController {
    @Autowired
    private moMoService momoService;

    @Autowired
    private orderService orderService;
    @Autowired
    private cartService cartService;

    @PostMapping("/payment/momo/create")
    @ResponseBody
    public ResponseEntity<?> createMomoPayment(@RequestParam Map<String, String> params, HttpSession session) {
        try {
            // 1. Lấy thông tin người dùng và giỏ hàng từ session
            loginCustomerEntity loginCustomer = (loginCustomerEntity) session.getAttribute("customer");
            if (loginCustomer == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Vui lòng đăng nhập."));
            }
            Integer userId = loginCustomer.getCustomer().getId();

            Map<String, String> shippingInfo = (Map<String, String>) session.getAttribute("shippingInfo");
            if (shippingInfo == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Phiên làm việc hết hạn."));
            }

            // Lấy paymentMethodId và discountId
            Integer paymentMethodId = Integer.valueOf(params.get("paymentMethod"));
            Integer discountId = params.get("discountId") != null && !params.get("discountId").isEmpty()
                    ? Integer.valueOf(params.get("discountId"))
                    : null;

            // 2. Tạo một đơn hàng ở trạng thái "Chờ thanh toán"
            orderEntity order = orderService.createPendingOrder(userId, shippingInfo, paymentMethodId, discountId);

            // 3. Chuẩn bị thông tin để gửi tới MoMo
            String orderId = order.getMaDonHang();
            String amount = String.valueOf(order.getTongTien().longValue());
            String orderInfo = "Thanh toán đơn hàng #" + order.getMaDonHang();
            String redirectUrl = "http://localhost:8080/payment/momo/return"; // Thay bằng domain của bạn
            String ipnUrl = "http://localhost:8080/payment/momo/return"; // Thay bằng domain của bạn

            // 4. Gọi MoMo Service để lấy payUrl
            String payUrl = momoService.createMomoPayment(orderId, amount, orderInfo, redirectUrl, ipnUrl);

            // 5. Trả về payUrl cho frontend
            return ResponseEntity.ok(Map.of("payUrl", payUrl));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Có lỗi xảy ra: " + e.getMessage()));
        }
    }

    @GetMapping("/payment/momo/return")
    public String momoReturn(Model model, @RequestParam Map<String, String> params, HttpSession session,
            RedirectAttributes redirectAttributes) {

        String orderIdStr = params.get("orderId");
        String resultCode = params.get("resultCode");

        // Luôn tìm đơn hàng để xử lý
        orderEntity order = orderService.getOrderByMaDonHang(orderIdStr);
        if (order == null) {
            // Xử lý trường hợp không tìm thấy đơn hàng
            redirectAttributes.addFlashAttribute("paymentError", "Không tìm thấy đơn hàng tương ứng.");
            return "redirect:/home";
        }

        if ("0".equals(resultCode)) { // resultCode = 0 là thanh toán thành công

            // 1. Cập nhật trạng thái đơn hàng thành "Chờ xử lý"
            Integer customerId = order.getKhachHang().getId();
            orderService.processSuccessfulOnlinePayment(order.getId(), customerId);

            // 3. Dọn dẹp session
            session.removeAttribute("shippingInfo");

            // 4. Gửi thông báo và thông tin đơn hàng ra view thành công
            model.addAttribute("titleMessage", "Thanh toán thành công!");
            model.addAttribute("detailMessage", "Đơn hàng của bạn đã được thanh toán và đang chờ xử lý.");
            model.addAttribute("order", order);

            model.addAttribute("view", "user/products/order-sucess");
            return "user/layout/main";

        } else { // Thanh toán thất bại
            redirectAttributes.addFlashAttribute("paymentError",
                    "Thanh toán qua MoMo không thành công. Vui lòng thử lại hoặc chọn một phương thức thanh toán khác.");

            // Chuyển hướng người dùng trở lại trang thanh toán
            return "redirect:/payment"; // Đảm bảo URL này là đúng
        }
    }
}
