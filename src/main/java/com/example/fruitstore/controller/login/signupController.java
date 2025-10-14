package com.example.fruitstore.controller.login;

import com.example.fruitstore.service.signupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("")
public class signupController {
	@Autowired
	private signupService signupService;

	@PostMapping("/signup")
	public ModelAndView signup(@RequestParam("ho_ten") String hoTen,
							  @RequestParam("so_dien_thoai") String soDienThoai,
							  @RequestParam("email") String email,
							  @RequestParam("ten_dang_nhap") String tenDangNhap,
							  @RequestParam("mat_khau") String matKhau,
							  @RequestParam("nhap_lai_mat_khau") String nhapLaiMatKhau) {
		String error = signupService.register(hoTen, soDienThoai, email, tenDangNhap, matKhau, nhapLaiMatKhau);
		ModelAndView mav = new ModelAndView("signin-up/login");
		if (error != null) {
			mav.addObject("signupError", error);
		} else {
			mav.addObject("signupSuccess", "Đăng ký thành công! Bạn có thể đăng nhập.");
		}
		mav.addObject("showSignup", true); // Để giao diện chuyển sang tab đăng ký nếu có lỗi
		return mav;
	}
}
