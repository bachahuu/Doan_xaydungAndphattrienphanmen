package com.example.fruitstore.controller.admin;

import com.example.fruitstore.entity.accountShopEntity;
import com.example.fruitstore.service.accountShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class adminAccountShopController {
    private static final Logger logger = LoggerFactory.getLogger(adminAccountShopController.class);

    @Autowired
    private accountShopService accountShopService;

    @GetMapping("/admin/account")
    public String showAccountList(Model model) {
        logger.info("Fetching all accounts via GET /admin/account");
        List<accountShopEntity> accounts = accountShopService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        model.addAttribute("view", "admin/products/manage_account");
        return "admin/layout/main";
    }

    @GetMapping("/admin/account/{id}")
    public ResponseEntity<accountShopEntity> getAccountById(@PathVariable Integer id) {
        logger.info("Fetching account by ID: {}", id);
        Optional<accountShopEntity> account = accountShopService.getAccountById(id);
        return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/admin/account/add")
    public ResponseEntity<?> addAccount(@RequestBody accountShopEntity account) {
        logger.info("Received POST request to /admin/account/add with data: {}", account);
        try {
            accountShopEntity newAccount = accountShopService.addAccount(account);
            logger.info("Account added successfully: {}", newAccount);
            return ResponseEntity.ok(newAccount);
        } catch (Exception e) {
            logger.error("Error adding account: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/admin/account/update")
    public ResponseEntity<?> updateAccount(@RequestBody accountShopEntity account) {
        logger.info("Received PUT request to /admin/account/update with data: {}", account);
        try {
            accountShopEntity updatedAccount = accountShopService.updateAccount(account);
            logger.info("Account updated successfully: {}", updatedAccount);
            return ResponseEntity.ok(updatedAccount);
        } catch (Exception e) {
            logger.error("Error updating account: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/admin/account/delete/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Integer id) {
        logger.info("Received DELETE request to /admin/account/delete/{}", id);
        try {
            accountShopService.deleteAccount(id);
            logger.info("Account with ID {} deleted successfully", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting account with ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}