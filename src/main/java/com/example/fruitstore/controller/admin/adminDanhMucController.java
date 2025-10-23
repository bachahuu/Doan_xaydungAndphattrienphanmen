package com.example.fruitstore.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fruitstore.entity.DanhMuc;
import com.example.fruitstore.service.DanhMucService;

@RestController
@RequestMapping("/api/admin/danhmuc")
public class adminDanhMucController {
    @Autowired
    private DanhMucService danhMucService;

    @GetMapping("/list")
    public List<DanhMuc> getAllDanhMuc() {
        return danhMucService.getAllDanhMuc();
    }

    @GetMapping("/get/{maDanhMuc}")
    public ResponseEntity<DanhMuc> getByMaDanhMuc(@PathVariable String maDanhMuc) {
        try {
            DanhMuc dm = danhMucService.getDanhMucByMaDanhMuc(maDanhMuc);
            return ResponseEntity.ok(dm);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addDanhMuc(@RequestBody DanhMuc danhMuc) {
        try {
            DanhMuc newDm = danhMucService.addDanhMuc(danhMuc);
            return ResponseEntity.ok(newDm);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{maDanhMuc}")
    public ResponseEntity<?> updateDanhMuc(@PathVariable String maDanhMuc, @RequestBody DanhMuc danhMuc) {
        try {
            DanhMuc updated = danhMucService.updateDanhMuc(maDanhMuc, danhMuc);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{maDanhMuc}")
    public ResponseEntity<?> deleteDanhMuc(@PathVariable String maDanhMuc) {
        try {
            danhMucService.deleteByMaDanhMuc(maDanhMuc);
            return ResponseEntity.ok().body("Xóa danh mục thành công");
        } catch (Exception e) {
            // Khối này sẽ bắt RuntimeException từ service và gửi thông báo lỗi về
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Fallback mapping to catch requests to /delete/ or /delete (without id)
    @DeleteMapping({"/delete", "/delete/"})
    public ResponseEntity<?> deleteDanhMucMissingId() {
        return ResponseEntity.status(400).body("Missing maDanhMuc in path");
    }
}
