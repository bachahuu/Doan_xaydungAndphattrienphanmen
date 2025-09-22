package com.example.fruitstore.controller.admin;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.fruitstore.entity.supplierEntity;
import com.example.fruitstore.service.supplierService;

@RestController
@RequestMapping("api/admin/supplier")
public class adminSupplierController {
    @Autowired
    private supplierService supplierService;

    @GetMapping("/list")
    public List<supplierEntity> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @PutMapping("/update/{maNCC}")
    public ResponseEntity<?> updateSupplier(@PathVariable String maNCC, @RequestBody supplierEntity supplier) {
        try {
            supplierEntity updatedSupplier = supplierService.updateSupplier(maNCC, supplier);
            return ResponseEntity.ok(updatedSupplier);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/{maNCC}")
    public ResponseEntity<supplierEntity> getSupplierByMaNCC(@PathVariable String maNCC) {
        try {
            supplierEntity supplier = supplierService.getSupplierByMaNCC(maNCC);
            return ResponseEntity.ok(supplier);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{maNCC}")
    public ResponseEntity<?> deleteSupplier(@PathVariable String maNCC) {
        try {
            supplierService.deleteSupplier(maNCC);
            return ResponseEntity.ok().body("Xóa nhà cung cấp thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addSupplier(@RequestBody supplierEntity supplier) {
        try {
            supplierEntity newSupplier = supplierService.addSupplier(supplier);
            return ResponseEntity.ok(newSupplier);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
