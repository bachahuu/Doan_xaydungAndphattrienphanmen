
package com.example.fruitstore.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Collections;

import com.example.fruitstore.dto.ProductDto;
import com.example.fruitstore.entity.SanPham;
import com.example.fruitstore.service.SanPhamService;

@Controller
public class SanPhamController {
    @Autowired
    private SanPhamService sanPhamService;

    // BinhLuanService removed

    @org.springframework.beans.factory.annotation.Autowired
    private com.example.fruitstore.respository.DanhMucRepository danhMucRepository;

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
        // comments feature removed
        return "user/layout/main";
    }

    @GetMapping("/menu")
    public String showMenu(
            @org.springframework.web.bind.annotation.RequestParam(value = "danhMucId", required = false) Integer danhMucId,
            Model model) {
        List<SanPham> sanPhamList = sanPhamService.getSanPhamByDanhMucId(danhMucId);
        model.addAttribute("sanPhamList", sanPhamList);
        model.addAttribute("selectedDanhMucId", danhMucId);
        // load categories for filter UI
        model.addAttribute("danhMucList", danhMucRepository.findAll());
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

    // JSON API for products (used by AJAX filter)
    @GetMapping("/api/products")
    @ResponseBody
    public java.util.List<ProductDto> apiProducts(
            @RequestParam(value = "danhMucId", required = false) Integer danhMucId) {
        return sanPhamService.getSanPhamByDanhMucId(danhMucId)
                .stream()
                .map(sp -> new ProductDto(sp))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/search/autocomplete")
    @ResponseBody
    public List<ProductDto> autocompleteSearch(@RequestParam(value = "query", required = false) String query) {
        if (query == null || query.trim().isEmpty() || query.length() < 2) {
            return Collections.emptyList(); // Không tìm nếu query quá ngắn
        }

        return sanPhamService.searchSanPhamByName(query)
                .stream()
                .map(sp -> new ProductDto(sp)) // Dùng ProductDto cho gọn
                .collect(Collectors.toList());
    }
}
