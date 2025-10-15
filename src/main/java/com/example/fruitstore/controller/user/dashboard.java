package com.example.fruitstore.controller.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.fruitstore.entity.CustomerEntity;
import com.example.fruitstore.entity.discountEntity;
import com.example.fruitstore.entity.loginCustomerEntity;
import com.example.fruitstore.entity.cart.cartDetailEntity;
import com.example.fruitstore.entity.cart.cartEntity;
import com.example.fruitstore.entity.phuongThucThanhToan;
import com.example.fruitstore.service.cartService;
import com.example.fruitstore.service.discountService;
import com.example.fruitstore.service.phuongThucThanhToanService;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class dashboard {
    @Autowired
    private cartService cartService;
    @Autowired
    private discountService discountService;
    @Autowired
    private phuongThucThanhToanService phuongThucThanhToanService;

    @GetMapping("/menu-static")
    public String showMenu(Model model) {
        model.addAttribute("view", "user/products/menu");
        return "user/layout/main";
    }

    @GetMapping("/home-static")
    public String showHome(Model model) {
        model.addAttribute("view", "user/products/home");
        return "user/layout/main";
    }

    @GetMapping("/cart")
    public String showCart(Model model, HttpSession session) {
        // Lấy ID khách hàng hiện tại
        loginCustomerEntity loginCustomer = (loginCustomerEntity) session.getAttribute("customer");
        cartEntity userCart = null; // 1. Khởi tạo giỏ hàng là null

        if (loginCustomer != null) {
            CustomerEntity customer = loginCustomer.getCustomer();
            if (customer != null) {
                Integer khachHangId = customer.getId();
                // Lấy giỏ hàng của người dùng cụ thể
                userCart = cartService.getCartByKhachHangId(khachHangId);
            }
        }
        model.addAttribute("carts", userCart);
        model.addAttribute("view", "user/products/cart");
        return "user/layout/main";
    }

    @GetMapping("/contact")
    public String showContact(Model model) {
        model.addAttribute("view", "user/products/contact");
        return "user/layout/main";
    }

    @GetMapping("/about")
    public String showAbout(Model model) {
        model.addAttribute("view", "user/products/about");
        return "user/layout/main";
    }

    @GetMapping("/location")
    public String showlocation(Model model, HttpSession session) {
        // 1. Lấy ID khách hàng hiện tại (giả sử là 1)
        loginCustomerEntity loginCustomer = (loginCustomerEntity) session.getAttribute("customer");
        cartEntity userCart = null;
        if (loginCustomer != null) {
            CustomerEntity customer = loginCustomer.getCustomer();
            if (customer != null) {
                // Lấy ID của người dùng đã đăng nhập
                Integer khachHangId = customer.getId();
                userCart = cartService.getCartByKhachHangId(khachHangId);
            }
        }

        double tamtinh = 0.0;
        if (userCart != null && userCart.getCartDetail() != null) {
            for (cartDetailEntity detail : userCart.getCartDetail()) {
                tamtinh += detail.getSoLuong() * detail.getSanPham().getGia();
            }
        }
        // 3. Đưa tổng tiền ("Tạm tính") vào Model
        model.addAttribute("cartTotalPrice", tamtinh);
        // 2. Lấy danh sách khuyến mãi từ service
        List<discountEntity> activeDiscounts = discountService.getAllDiscounts().stream()
                .filter(d -> d.getTrangThai() == 1) // Lọc những mã còn hoạt động
                .collect(Collectors.toList());
        model.addAttribute("discounts", activeDiscounts);
        model.addAttribute("view", "user/products/location");
        return "user/layout/main";
    }

    // TẠO PHƯƠNG THỨC MỚI ĐỂ XỬ LÝ SUBMIT TỪ TRANG LOCATION
    @PostMapping("/shipping/submit")
    public String handleShippingInfo(
            @RequestParam Map<String, String> shippingData,
            HttpSession session) {
        // shippingData sẽ chứa tất cả dữ liệu từ form
        // txtname, txtemail, finalTotal, discountId...

        // Lưu toàn bộ thông tin này vào session để trang payment có thể lấy ra
        session.setAttribute("shippingInfo", shippingData);

        // Chuyển hướng đến trang payment
        return "redirect:/payment";
    }

    @GetMapping("/payment")
    public String showPayment(Model model, HttpSession session) {
        // 1. Lấy thông tin đã lưu từ session
        @SuppressWarnings("unchecked") // Bỏ cảnh báo casting
        Map<String, String> shippingInfo = (Map<String, String>) session.getAttribute("shippingInfo");

        if (shippingInfo == null) {
            // Nếu không có thông tin trong session, quay lại trang giỏ hàng hoặc location
            return "redirect:/location";
        }
        // 2. Lấy ID khách hàng hiện tại (giả sử là 1)
        loginCustomerEntity loginCustomer = (loginCustomerEntity) session.getAttribute("customer");
        cartEntity userCart = null;
        // Lấy thông tin giỏ hàng để hiển thị sản phẩm
        if (loginCustomer != null) {
            CustomerEntity customer = loginCustomer.getCustomer();
            if (customer != null) {
                // Lấy ID của người dùng đã đăng nhập
                Integer khachHangId = customer.getId();
                userCart = cartService.getCartByKhachHangId(khachHangId);
            }
        }

        if (userCart != null) {
            model.addAttribute("cartItems", userCart.getCartDetail());
        }

        // 3. Đưa thông tin từ session vào model để hiển thị ra view
        model.addAttribute("shippingInfo", shippingInfo);
        // Lấy tổng tiền cuối cùng từ session
        double finalTotal = Double.parseDouble(shippingInfo.getOrDefault("finalTotal", "0"));
        model.addAttribute("finalTotal", finalTotal);

        // Lấy danh sách phương thức thanh toán từ service
        List<phuongThucThanhToan> paymentMethods = phuongThucThanhToanService.findAllActive();
        model.addAttribute("paymentMethods", paymentMethods);

        model.addAttribute("view", "user/products/payment");
        return "user/layout/main";
    }

    @GetMapping("/news")
    public String showNews(Model model) {
        model.addAttribute("view", "user/products/news");
        return "user/layout/main";
    }

}
