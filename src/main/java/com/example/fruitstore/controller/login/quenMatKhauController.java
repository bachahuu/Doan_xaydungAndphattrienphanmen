package com.example.fruitstore.controller.login;

import com.example.fruitstore.service.quenMatKhauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class quenMatKhauController {
    @Autowired
    private quenMatKhauService service;

    // Hiển thị form nhập username/email
    @GetMapping("/quen-mat-khau")
    public ModelAndView showEmailForm() {
        return new ModelAndView("signin-up/Quenmatkhau_IndexEmail");
    }

    // Xử lý form nhập username/email
    @PostMapping("/quen-mat-khau")
    public ModelAndView handleEmailForm(
            @RequestParam("ten_dang_nhap") String username,
            @RequestParam("email") String email) {
        Integer accountId = service.checkAccount(username, email);
        if (accountId == null) {
            ModelAndView mv = new ModelAndView("signin-up/Quenmatkhau_IndexEmail");
            mv.addObject("error", "Tên đăng nhập hoặc email không đúng!");
            return mv;
        }
        // Lưu accountId vào session để dùng cho bước tiếp theo
        ModelAndView mv = new ModelAndView("signin-up/Quenmatkhau_IndexDatLai");
        mv.addObject("accountId", accountId);
        return mv;
    }

    // Xử lý đặt lại mật khẩu
    @PostMapping("/dat-lai-mat-khau")
    public ModelAndView handleResetPassword(
            @RequestParam("accountId") int accountId,
            @RequestParam("mat_khau_moi") String newPassword,
            @RequestParam("nhap_lai_mat_khau") String confirmPassword) {
        ModelAndView mv = new ModelAndView("signin-up/Quenmatkhau_IndexDatLai");
        mv.addObject("accountId", accountId);
        if (!newPassword.equals(confirmPassword)) {
            mv.addObject("error", "Mật khẩu nhập lại không khớp!");
            return mv;
        }
        boolean ok = service.resetPassword(accountId, newPassword);
        if (ok) {
            mv = new ModelAndView("redirect:/login?resetSuccess");
        } else {
            mv.addObject("error", "Có lỗi xảy ra, vui lòng thử lại!");
        }
        return mv;
    }
}
