package com.example.fruitstore.service;
import org.springframework.transaction.annotation.Transactional;


import com.example.fruitstore.entity.loginCustomerEntity;
import com.example.fruitstore.entity.CustomerEntity;
import com.example.fruitstore.respository.signupRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class signupService {
	@Autowired
	private signupRespository signupRespository;

	// Validate đầu vào, trả về null nếu hợp lệ, trả về lỗi nếu không hợp lệ
	public String validateInput(String username, String password, String rePassword, String email, String phone) {
		// Kiểm tra ký tự lạ SQLi/XSS
		String pattern = ".*[<>\\'\";\\-\\/].*";
		if (username.matches(pattern) || password.matches(pattern)) {
			return "Thông tin chứa ký tự không hợp lệ.";
		}
		// Kiểm tra mật khẩu nhập lại
		if (!password.equals(rePassword)) {
			return "Mật khẩu nhập lại không khớp.";
		}
		// Kiểm tra số điện thoại
		if (!phone.matches("^\\d{10}$")) {
			return "Số điện thoại phải là 10 số.";
		}
		// Kiểm tra email
		if (!email.endsWith("@gmail.com")) {
			return "Email phải có đuôi @gmail.com.";
		}
		// Kiểm tra trùng tên đăng nhập
		if (signupRespository.existsByUsername(username)) {
			return "Tên đăng nhập đã tồn tại.";
		}
		return null;
	}

	@Transactional
	public String register(String hoTen, String soDienThoai, String email, String tenDangNhap, String matKhau, String nhapLaiMatKhau) {
		String error = validateInput(tenDangNhap, matKhau, nhapLaiMatKhau, email, soDienThoai);
		if (error != null) return error;

		// Lưu vào bảng tài khoản
		loginCustomerEntity account = new loginCustomerEntity();
		account.setUsername(tenDangNhap);
		account.setPassword(matKhau);
		signupRespository.saveAccount(account);

	// Lưu vào bảng khách hàng
	CustomerEntity customer = new CustomerEntity();
	customer.setTenKhachHang(hoTen);
	customer.setSoDienThoai(soDienThoai);
	customer.setEmail(email);
	customer.setTaiKhoanId(account.getId());
	customer.setLoaiKhachHang("moi");
	customer.setTrangThaiTaiKhoan(1);
	// Sinh mã khách hàng tự động (ví dụ: KH + id tài khoản + thời gian)
	String maKhachHang = "KH" + account.getId() + System.currentTimeMillis();
	customer.setMaKhachHang(maKhachHang);
	signupRespository.saveCustomer(customer);

		return null;
	}
}