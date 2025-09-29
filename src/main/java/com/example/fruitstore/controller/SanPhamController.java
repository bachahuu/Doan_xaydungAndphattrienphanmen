
package com.example.fruitstore.controller;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.fruitstore.entity.SanPham;
import com.example.fruitstore.service.SanPhamService;

@Controller
public class SanPhamController {
    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") Integer id, Model model) {
        SanPham product = sanPhamService.getSanPhamById(id);
        if (product == null) {
            return "redirect:/menu";
        }
        // Lấy danh sách gợi ý (8 sản phẩm khác)
        List<SanPham> suggestList = sanPhamService.getAllSanPham()
            .stream()
            .filter(sp -> !sp.getId().equals(id))
            .limit(8)
            .collect(Collectors.toList());
        model.addAttribute("product", product);
        model.addAttribute("suggestList", suggestList);
        model.addAttribute("view", "user/products/product_detail");
        return "user/layout/main";
    }

    @GetMapping("/menu")
    public String showMenu(Model model) {
    List<SanPham> sanPhamList = sanPhamService.getAllSanPham();
    model.addAttribute("sanPhamList", sanPhamList);
    model.addAttribute("view", "user/products/menu");
    return "user/layout/main";
    }

    @GetMapping("/home")
    public String showHome(Model model) {
    List<SanPham> sanPhamList = sanPhamService.getAllSanPham();
    model.addAttribute("sanPhamList", sanPhamList);
    model.addAttribute("view", "user/products/home");
    return "user/layout/main";
    }
}
