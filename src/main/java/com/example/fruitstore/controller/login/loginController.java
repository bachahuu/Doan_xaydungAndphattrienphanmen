package com.example.fruitstore.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
// import com.example.fruitstore.entity.loginShopEntity;
import com.example.fruitstore.entity.loginCustomerEntity;
import com.example.fruitstore.service.loginService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class loginController {

    @Autowired
    private loginService loginService;

    @Autowired
    private com.example.fruitstore.respository.CustomerRepository customerRepository;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView showLogin() {
        return new ModelAndView("signin-up/login");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam("so_dien_thoai") String username,
            @RequestParam("mat_khau") String password,
            HttpSession session) {
        Object user = loginService.authenticate(username, password);
        if (user == null) {
            ModelAndView mav = new ModelAndView("signin-up/login");
            mav.addObject("error", "Sai tài khoản hoặc mật khẩu!");
            return mav;
        }
        if (user instanceof com.example.fruitstore.entity.loginShopEntity) {
            com.example.fruitstore.entity.loginShopEntity shop = (com.example.fruitstore.entity.loginShopEntity) user;
            session.setAttribute("shopUser", shop); // Lưu toàn bộ entity vào session để truy cập role
            if (shop.getRole() != null && !shop.getRole().isEmpty()) {
                return new ModelAndView("redirect:/admin/account"); // Redirect đến dashboard chung
            }
        }
        if (user instanceof com.example.fruitstore.entity.loginCustomerEntity) {
            // loginCustomerEntity customer = (loginCustomerEntity) user;

            // System.out.println("Setting session attribute for customer: " +
            // customer.getUsername());
            // session.setAttribute("customer", customer);
            loginCustomerEntity account = (loginCustomerEntity) user;

            // 🟩 Tìm CustomerEntity tương ứng qua taiKhoanId
            com.example.fruitstore.entity.CustomerEntity customerEntity = customerRepository.findAll().stream()
                    .filter(c -> Integer.valueOf(account.getId()).equals(c.getTaiKhoanId()))
                    .findFirst()
                    .orElse(null);

            // 🟦 Lưu cả hai vào session
            session.setAttribute("account", account);
            session.setAttribute("customerEntity", customerEntity);
            return new ModelAndView("redirect:/home-static");
        }
        return new ModelAndView("redirect:/home-static");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home-static";
    }

    @RequestMapping(value = "/logout-shop", method = RequestMethod.GET)
    public String logoutShop(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @RequestMapping(value = "/user-info", method = RequestMethod.GET)
    public ModelAndView showUserInfo(HttpSession session) {
        loginCustomerEntity customer = (loginCustomerEntity) session.getAttribute("customer");
        if (customer == null) {
            return new ModelAndView("redirect:/login");
        }
        ModelAndView mav = new ModelAndView("user/user-info");
        mav.addObject("customer", customer);
        return mav;
    }

    @GetMapping("/my-account")
    public ModelAndView showMyAccount(HttpSession session) {
        com.example.fruitstore.entity.CustomerEntity customerEntity = (com.example.fruitstore.entity.CustomerEntity) session
                .getAttribute("customerEntity");

        if (customerEntity == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelAndView mav = new ModelAndView("user/layout/main");
        mav.addObject("view", "user/products/my-account");
        mav.addObject("customer", customerEntity);
        return mav;
    }

    @GetMapping("/addresses")
    public ModelAndView showAddresses(HttpSession session) {
        loginCustomerEntity customer = (loginCustomerEntity) session.getAttribute("customer");
        if (customer == null) {
            return new ModelAndView("redirect:/login");
        }
        ModelAndView mav = new ModelAndView("user/addresses");
        mav.addObject("customer", customer);
        return mav;
    }

    @PostMapping("/my-account/update")
    public ModelAndView updateCustomerInfo(
            @ModelAttribute("customer") com.example.fruitstore.entity.CustomerEntity updatedCustomer,
            HttpSession session) {

        com.example.fruitstore.entity.CustomerEntity existing = (com.example.fruitstore.entity.CustomerEntity) session
                .getAttribute("customerEntity");

        if (existing == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelAndView mav = new ModelAndView("user/layout/main");
        mav.addObject("view", "user/products/my-account");

        // 🟩 Kiểm tra rỗng
        if (updatedCustomer.getTenKhachHang() == null || updatedCustomer.getTenKhachHang().trim().isEmpty()) {
            mav.addObject("error", "Tên khách hàng không được để trống!");
            mav.addObject("customer", existing);
            return mav;
        }

        if (updatedCustomer.getSoDienThoai() == null || updatedCustomer.getSoDienThoai().trim().isEmpty()) {
            mav.addObject("error", "Số điện thoại không được để trống!");
            mav.addObject("customer", existing);
            return mav;
        }

        // 🟦 Nếu hợp lệ → cập nhật
        existing.setTenKhachHang(updatedCustomer.getTenKhachHang());
        existing.setEmail(updatedCustomer.getEmail());
        existing.setSoDienThoai(updatedCustomer.getSoDienThoai());
        existing.setDiaChi(updatedCustomer.getDiaChi());

        customerRepository.save(existing);
        session.setAttribute("customerEntity", existing);

        mav.addObject("customer", existing);
        mav.addObject("success", "Cập nhật thông tin thành công!");
        return mav;
    }

    @RequestMapping(value = "/forgotpassword", method = RequestMethod.GET)
    public String showRegister() {
        return "signin-up/Quenmatkhau_IndexEmail";
    }

}