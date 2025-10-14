package com.example.fruitstore.service;

import com.example.fruitstore.entity.loginCustomerEntity;
import com.example.fruitstore.entity.CustomerEntity;
import com.example.fruitstore.respository.quenMatKhauRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class quenMatKhauService {
    @Autowired
    private quenMatKhauRespository repo;

    // Kiểm tra username và email có khớp không
    public Integer checkAccount(String username, String email) {
        loginCustomerEntity account = repo.findAccountByUsername(username);
        if (account == null) return null;
        CustomerEntity customer = repo.findCustomerByAccountId(account.getId());
        if (customer == null) return null;
        if (!customer.getEmail().equalsIgnoreCase(email)) return null;
        return account.getId();
    }

    // Đổi mật khẩu
    public boolean resetPassword(int accountId, String newPassword) {
        try {
            repo.updatePassword(accountId, newPassword);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
