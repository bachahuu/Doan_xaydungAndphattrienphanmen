package com.example.fruitstore.service;

import com.example.fruitstore.entity.accountShopEntity;
import com.example.fruitstore.respository.AccountShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class accountShopService {
    @Autowired
    private AccountShopRepository accountShopRepository;

    public List<accountShopEntity> getAllAccounts() {
        return accountShopRepository.findAll();
    }

    public Optional<accountShopEntity> getAccountById(Integer id) {
        return accountShopRepository.findById(id);
    }

    public accountShopEntity addAccount(accountShopEntity account) throws Exception {
        if (accountShopRepository.existsByUsername(account.getUsername())) {
            throw new Exception("Tên đăng nhập đã tồn tại");
        }
        if (account.getPassword().length() < 6) {
            throw new Exception("Mật khẩu phải có ít nhất 6 ký tự");
        }
        return accountShopRepository.save(account);
    }

    public accountShopEntity updateAccount(accountShopEntity account) throws Exception {
        if (!accountShopRepository.existsById(account.getId())) {
            throw new Exception("Tài khoản không tồn tại");
        }
        if (account.getPassword().length() < 6) {
            throw new Exception("Mật khẩu phải có ít nhất 6 ký tự");
        }
        // Vì username readonly, không kiểm tra trùng
        return accountShopRepository.save(account);
    }

    public void deleteAccount(Integer id) throws Exception {
        if (!accountShopRepository.existsById(id)) {
            throw new Exception("Tài khoản không tồn tại");
        }
        accountShopRepository.deleteById(id);
    }
}