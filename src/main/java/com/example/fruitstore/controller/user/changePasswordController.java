package com.example.fruitstore.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.fruitstore.entity.loginCustomerEntity;
import com.example.fruitstore.respository.loginRespository;
import jakarta.servlet.http.HttpSession;

@Controller
public class changePasswordController {  // Đã chuẩn hóa tên class

    @Autowired
    private loginRespository loginRepository;  // Sử dụng repository để update

    // Không cần loginService nếu không dùng ở đây

    @GetMapping("/change-password")
    public String showChangePasswordForm(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        loginCustomerEntity customer = (loginCustomerEntity) session.getAttribute("customer");
        if (customer == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để đổi mật khẩu.");
            return "redirect:/login";
        }
        model.addAttribute("customer", customer);
        model.addAttribute("view", "user/products/change-password");
        return "user/layout/main";
    }

    @PostMapping("/change-password/update")  // Khớp URL với GET, dùng POST cho submit
    public String updatePassword(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        loginCustomerEntity customer = (loginCustomerEntity) session.getAttribute("customer");
        if (customer == null) {
            redirectAttributes.addFlashAttribute("error", "Phiên đăng nhập hết hạn.");
            return "redirect:/login";
        }

        // Kiểm tra mật khẩu cũ (plain text)
        if (!customer.getPassword().equals(oldPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không đúng.");
            return "redirect:/change-password";
        }

        // Kiểm tra mật khẩu mới khớp confirm
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới không khớp.");
            return "redirect:/change-password";
        }

        // Cập nhật mật khẩu mới (plain text)
        customer.setPassword(newPassword);
        loginRepository.updateCustomer(customer);  // Gọi method mới để merge/update

        // Update session để đồng bộ (nếu entity thay đổi reference)
        session.setAttribute("customer", customer);

        redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công.");
        return "redirect:/my-account";
    }
}