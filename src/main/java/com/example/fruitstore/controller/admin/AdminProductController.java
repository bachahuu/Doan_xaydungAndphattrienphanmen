package com.example.fruitstore.controller.admin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.fruitstore.dto.ProductDto;
import com.example.fruitstore.entity.DanhMuc;
import com.example.fruitstore.entity.SanPham;
import com.example.fruitstore.service.DanhMucService;
import com.example.fruitstore.service.SanPhamService;
import com.example.fruitstore.service.supplierService;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class AdminProductController {

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/images";

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private DanhMucService danhMucService;

    @Autowired
    private supplierService supplierService;

    // Phương thức liệt kê sản phẩm (Giữ nguyên)
    @GetMapping("/admin/products")
    public String listSanPham(Model model) {
        List<SanPham> sanPhamList = sanPhamService.getAllSanPham();
        model.addAttribute("sanPhamList", sanPhamList);
        return "admin/products/manage_product";
    }

    // =================================================================
    // PHƯƠNG THỨC MỚI: Trả về fragment form cho Thêm mới (Dùng cho AJAX)
    // =================================================================
    @GetMapping("/admin/products/form/new")
    public String showNewProductForm(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("product", productDto);
        model.addAttribute("danhMucList", danhMucService.getAllDanhMuc());
        model.addAttribute("nhaCungCapList", supplierService.getAllSuppliers());
        // Trả về fragment "content" của form_product.html
        return "admin/products/form_product :: content";
    }

    // =================================================================
    // PHƯƠNG THỨC MỚI: Trả về fragment form cho Sửa (Dùng cho AJAX)
    // =================================================================
    @GetMapping("/admin/products/form/{id}")
    public String showEditProductForm(@PathVariable("id") Integer id, Model model) {
        SanPham product = sanPhamService.getSanPhamById(id);
        if (product == null) {
            // Có thể chuyển hướng đến trang lỗi hoặc danh sách nếu không tìm thấy
            return "redirect:/admin/products";
        }

        // Chuyển SanPham Entity sang ProductDto
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setMaSanPham(product.getMaSanPham());
        productDto.setTenSanPham(product.getTenSanPham());
        productDto.setGia(product.getGia());
        productDto.setMoTa(product.getMoTa());
        productDto.setHinhAnh(product.getHinhAnh());
        productDto.setSoLuongTon(product.getSoLuongTon());

        if (product.getDanhMuc() != null) {
            productDto.setDanhMucId(product.getDanhMuc().getId());
        }
        if (product.getNhaCungCap() != null) {
            // Lưu ý: Dùng getSupplierById() từ supplierService
            productDto.setNhaCungCapId(product.getNhaCungCap().getId());
        }

        // Format LocalDateTime sang String theo định dạng HTML5 input
        // type="datetime-local" (yyyy-MM-dd'T'HH:mm)
        // ĐÃ SỬA LỖI: DateTimeFormatter.ofPattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        if (product.getNgayNhap() != null) {
            productDto.setNgayNhap(product.getNgayNhap().format(formatter));
        }
        if (product.getHanSuDung() != null) {
            productDto.setHanSuDung(product.getHanSuDung().format(formatter));
        }

        model.addAttribute("product", productDto);
        model.addAttribute("danhMucList", danhMucService.getAllDanhMuc());
        model.addAttribute("nhaCungCapList", supplierService.getAllSuppliers());

        return "admin/products/form_product :: content";
    }

    // =================================================================
    // PHƯƠNG THỨC CHỈNH SỬA: Xử lý save bằng AJAX và trả về JSON
    // =================================================================
    @PostMapping(value = "/admin/products/save", produces = "application/json")
    @ResponseBody // <--- QUAN TRỌNG: Để trả về JSON thay vì View/Redirect
    public Map<String, Object> saveProduct(@ModelAttribute("product") ProductDto productDto,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        Map<String, Object> response = new HashMap<>();
        try {
            SanPham product;
            if (productDto.getId() != null) {
                // Sửa: Lấy entity cũ
                product = sanPhamService.getSanPhamById(productDto.getId());
                if (product == null) {
                    throw new RuntimeException("Sản phẩm không tồn tại.");
                }
            } else {
                // Thêm mới
                product = new SanPham();
            }

            // 1. Copy data from DTO to Entity
            product.setMaSanPham(productDto.getMaSanPham());
            product.setTenSanPham(productDto.getTenSanPham());
            product.setGia(productDto.getGia());
            product.setMoTa(productDto.getMoTa());
            product.setSoLuongTon(productDto.getSoLuongTon());

            // 2. Xử lý upload ảnh
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
                Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, fileName);
                Files.write(fileNameAndPath, imageFile.getBytes());
                product.setHinhAnh(fileName);
            } else if (productDto.getHinhAnh() != null && !productDto.getHinhAnh().isEmpty()) {
                product.setHinhAnh(productDto.getHinhAnh()); // Giữ ảnh cũ
            } else {
                product.setHinhAnh(null);
            }

            // 3. Chuyển String ngày tháng (từ input type="datetime-local") sang
            // LocalDateTime
            // ĐÃ SỬA LỖI: DateTimeFormatter.ofPattern
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            if (productDto.getNgayNhap() != null && !productDto.getNgayNhap().isEmpty()) {
                product.setNgayNhap(LocalDateTime.parse(productDto.getNgayNhap(), formatter));
            } else {
                product.setNgayNhap(null);
            }
            if (productDto.getHanSuDung() != null && !productDto.getHanSuDung().isEmpty()) {
                product.setHanSuDung(LocalDateTime.parse(productDto.getHanSuDung(), formatter));
            } else {
                product.setHanSuDung(null);
            }

            // 4. Set Entity Danh Muc và Nha Cung Cap từ ID
            if (productDto.getDanhMucId() != null) {
                DanhMuc danhMuc = danhMucService.getDanhMucById(productDto.getDanhMucId());
                product.setDanhMuc(danhMuc);
            } else {
                product.setDanhMuc(null);
            }

            if (productDto.getNhaCungCapId() != null) {
                product.setNhaCungCap(supplierService.getSupplierById(productDto.getNhaCungCapId()));
            } else {
                product.setNhaCungCap(null);
            }

            // 5. Lưu sản phẩm
            sanPhamService.save(product);

            response.put("success", true);
            response.put("message", "Lưu sản phẩm thành công!");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Lỗi khi lưu sản phẩm: " + e.getMessage());
            return response;
        }
    }

    // Phương thức xóa sản phẩm (Giữ nguyên)
    @DeleteMapping("/admin/products/{id}")
    @ResponseBody
    public java.util.Map<String, Object> deleteProduct(@PathVariable("id") Integer id) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        try {
            sanPhamService.deleteById(id);
            response.put("success", true);
            response.put("message", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/admin/products/detail/{id}")
    public String viewProductDetail(@PathVariable("id") Integer id, Model model) {
        try {
            // Sử dụng phương thức có JOIN FETCH để đảm bảo tải đủ dữ liệu liên quan
            SanPham product = sanPhamService.getSanPhamByIdWithDetails(id);

            if (product == null) {
                // Xử lý khi không tìm thấy sản phẩm
                model.addAttribute("errorMessage", "Không tìm thấy sản phẩm có ID: " + id);
                return "fragments/error_message :: content";
            }

            model.addAttribute("product", product);

            // Sửa lỗi: Thêm tiền tố đường dẫn thư mục con
            return "admin/products/view_product_detail :: content";

        } catch (Exception e) {
            // ... (phần xử lý lỗi)
            System.err.println("LỖI SERVER KHI TẢI CHI TIẾT SẢN PHẨM ID " + id + ": " + e.getMessage());
            e.printStackTrace();

            model.addAttribute("errorMessage", "Đã xảy ra lỗi khi tải chi tiết sản phẩm: " + e.getMessage());
            return "fragments/error_message :: content";
        }
    }
}