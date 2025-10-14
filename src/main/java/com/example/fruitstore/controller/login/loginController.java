package com.example.fruitstore.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.example.fruitstore.entity.loginShopEntity;
import com.example.fruitstore.entity.loginCustomerEntity;
import com.example.fruitstore.service.loginService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class loginController {

    @Autowired
    private loginService loginService;

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
            if (shop.getRole() != null && !shop.getRole().isEmpty()) {
                return new ModelAndView("redirect:/admin/account");
            }
        }
        if (user instanceof com.example.fruitstore.entity.loginCustomerEntity) {
            loginCustomerEntity customer = (loginCustomerEntity) user;
            System.out.println("Setting session attribute for customer: " + customer.getUsername());
            session.setAttribute("customer", customer);
            return new ModelAndView("redirect:/home-static");
        }
        return new ModelAndView("redirect:/home-static");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home-static";
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
        loginCustomerEntity customer = (loginCustomerEntity) session.getAttribute("customer");
        if (customer == null) {
            return new ModelAndView("redirect:/login");
        }
        ModelAndView mav = new ModelAndView("user/my-account");
        mav.addObject("customer", customer);
        return mav;
    }

    @GetMapping("/addresses")
    public ModelAndView showAddresses(HttpSession session) {
        loginCustomerEntity customer = (loginCustomerEntity) session.getAttribute("customer");
        if (customer == null) {
            return new ModelAndView("redirect:/login");
        }
        // Assume you need to fetch addresses from CustomerEntity based on taiKhoanId
        // This requires a repository method to join taikhoankhachhang with khachhang
        ModelAndView mav = new ModelAndView("user/addresses");
        mav.addObject("customer", customer);
        return mav;
    }

    @RequestMapping(value = "/forgotpassword", method = RequestMethod.GET)
    public String showRegister() {
        return "signin-up/Quenmatkhau_IndexEmail";
    }
}