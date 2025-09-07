package com.example.fruitstore.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class loginController {

    @GetMapping("/login")
    public String showLogin() {
        return "signin-up/login"; // trả về login.html trực tiếp
    }

    @GetMapping("/forgotpassword")
    public String showRegister() {
        return "signin-up/Quenmatkhau_IndexEmail"; // trả về register.html trực tiếp
    }

}
