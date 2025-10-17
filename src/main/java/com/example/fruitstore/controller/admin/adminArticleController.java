package com.example.fruitstore.controller.admin;

import com.example.fruitstore.entity.articleEntity;
import com.example.fruitstore.entity.EmployeeEntity;
import com.example.fruitstore.service.articleService;
import com.example.fruitstore.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/admin/posts")
public class adminArticleController {
    private static final Logger logger = LoggerFactory.getLogger(adminArticleController.class);
    private static final String UPLOAD_DIR = "src/main/resources/static/images/uploads/";

    @Autowired
    private articleService articleService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public String showArticles(Model model) {
        List<articleEntity> articles = articleService.getAllArticles();
        List<EmployeeEntity> employees = employeeService.getAllEmployees();
        
        logger.info("Fetched {} articles from DB", articles.size());
        logger.info("Fetched {} employees from DB", employees.size());
        
        model.addAttribute("articles", articles);
        model.addAttribute("employees", employees);
        model.addAttribute("view", "admin/products/manage_article");
        return "admin/layout/main";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<articleEntity> getArticleById(@PathVariable Integer id) {
        logger.info("Fetching article by ID: {}", id);
        return articleService.getArticleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
            logger.info("Created upload directory: {}", UPLOAD_DIR);
        }
        
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = System.currentTimeMillis() + extension;
        
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        logger.info("Saved file: {} to {}", fileName, filePath);
        
        return "/images/uploads/" + fileName;
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addArticle(
            @RequestParam("maBaiViet") String maBaiViet,
            @RequestParam("tieuDe") String tieuDe,
            @RequestParam("noiDung") String noiDung,
            @RequestParam("ngayDang") String ngayDang,
            @RequestParam("nhanVienId") Integer nhanVienId,
            @RequestParam(value = "hinhAnh", required = false) MultipartFile hinhAnh) {
        try {
            logger.info("Adding new article: {} by employee ID: {}", maBaiViet, nhanVienId);
            
            articleEntity article = new articleEntity();
            article.setMaBaiViet(maBaiViet);
            article.setTieuDe(tieuDe);
            article.setNoiDung(noiDung);
            article.setNgayDang(java.sql.Date.valueOf(ngayDang));
            article.setNhanVienId(nhanVienId);
            
            if (hinhAnh != null && !hinhAnh.isEmpty()) {
                String imagePath = saveFile(hinhAnh);
                article.setHinhAnh(imagePath);
            }
            
            articleEntity savedArticle = articleService.saveArticle(article);
            logger.info("Added article with ID: {}", savedArticle.getId());
            return ResponseEntity.ok(savedArticle);
        } catch (IOException e) {
            logger.error("Error saving file: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Lỗi khi upload hình ảnh: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error adding article: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<?> updateArticle(
            @RequestParam("id") Integer id,
            @RequestParam("maBaiViet") String maBaiViet,
            @RequestParam("tieuDe") String tieuDe,
            @RequestParam("noiDung") String noiDung,
            @RequestParam("ngayDang") String ngayDang,
            @RequestParam("nhanVienId") Integer nhanVienId,
            @RequestParam(value = "hinhAnh", required = false) MultipartFile hinhAnh) {
        try {
            logger.info("Updating article with ID: {}", id);
            
            articleEntity article = articleService.getArticleById(id)
                    .orElseThrow(() -> new Exception("Bài viết không tồn tại"));
            
            article.setMaBaiViet(maBaiViet);
            article.setTieuDe(tieuDe);
            article.setNoiDung(noiDung);
            article.setNgayDang(java.sql.Date.valueOf(ngayDang));
            article.setNhanVienId(nhanVienId);
            
            if (hinhAnh != null && !hinhAnh.isEmpty()) {
                String imagePath = saveFile(hinhAnh);
                article.setHinhAnh(imagePath);
                logger.info("Updated image path: {}", imagePath);
            }
            
            articleEntity updatedArticle = articleService.saveArticle(article);
            logger.info("Successfully updated article with ID: {}", updatedArticle.getId());
            return ResponseEntity.ok(updatedArticle);
        } catch (IOException e) {
            logger.error("Error saving file: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Lỗi khi upload hình ảnh: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error updating article: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteArticle(@PathVariable Integer id) {
        try {
            articleService.deleteArticle(id);
            logger.info("Deleted article with ID: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting article: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}