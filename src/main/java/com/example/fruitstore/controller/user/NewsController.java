package com.example.fruitstore.controller.user;

import com.example.fruitstore.entity.articleEntity;
import com.example.fruitstore.service.articleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private articleService articleService;

    @GetMapping
    public String showNewsList(Model model) {
        // Chỉ lấy các bài viết đã đến ngày đăng
        List<articleEntity> articles = articleService.getPublishedArticles();

        List<Map<String, Object>> formattedArticles = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("vi", "VN"));

        for (articleEntity article : articles) {
            Map<String, Object> articleMap = new HashMap<>();
            articleMap.put("id", article.getId());
            articleMap.put("tieuDe", article.getTieuDe());
            
            String noiDung = article.getNoiDung();
            String excerpt = (noiDung != null) ? noiDung.substring(0, Math.min(200, noiDung.length())) + "..." : "";
            articleMap.put("excerpt", excerpt);
            
            String formattedDate = sdf.format(article.getNgayDang());
            articleMap.put("formattedDate", formattedDate);
            
            String hinhAnh = article.getHinhAnh() != null ? article.getHinhAnh() : "/images/default.jpg";
            articleMap.put("hinhAnh", hinhAnh);
            
            formattedArticles.add(articleMap);
        }

        model.addAttribute("articles", formattedArticles);
        model.addAttribute("view", "user/products/news");
        return "user/layout/main";
    }

    @GetMapping("/{id}")
    public String showNewsDetail(@PathVariable Integer id, Model model) {
        // Chỉ lấy bài viết nếu đã được publish
        Optional<articleEntity> articleOpt = articleService.getPublishedArticleById(id);
        
        if (articleOpt.isPresent()) {
            articleEntity article = articleOpt.get();
            Map<String, Object> articleMap = new HashMap<>();
            articleMap.put("tieuDe", article.getTieuDe());
            articleMap.put("noiDung", article.getNoiDung());
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("vi", "VN"));
            articleMap.put("formattedDate", sdf.format(article.getNgayDang()));
            
            String hinhAnh = article.getHinhAnh() != null ? article.getHinhAnh() : "/images/default.jpg";
            articleMap.put("hinhAnh", hinhAnh);
            
            model.addAttribute("article", articleMap);
            model.addAttribute("view", "user/products/news-detail");
            return "user/layout/main";
        }
        
        // Nếu bài viết chưa được publish hoặc không tồn tại
        return "redirect:/news";
    }
}