package com.example.fruitstore.service;

import com.example.fruitstore.entity.articleEntity;
import com.example.fruitstore.respository.articleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class articleService {
    private static final Logger logger = LoggerFactory.getLogger(articleService.class);

    @Autowired
    private articleRepository articleRepository;

    public List<articleEntity> getAllArticles() {
        List<articleEntity> articles = articleRepository.findAll();
        logger.info("Retrieved {} articles from database", articles.size());
        return articles;
    }

    public Optional<articleEntity> getArticleById(Integer id) {
        return articleRepository.findById(id);
    }

    public articleEntity saveArticle(articleEntity article) {
        return articleRepository.save(article);
    }

    public void deleteArticle(Integer id) {
        articleRepository.deleteById(id);
    }

    public Optional<articleEntity> getPublishedArticleById(Integer id) {
        Optional<articleEntity> articleOpt = articleRepository.findById(id);
        Date currentDate = Date.valueOf(LocalDate.now());
        
        if (articleOpt.isPresent()) {
            articleEntity article = articleOpt.get();
            // Chỉ trả về nếu ngày đăng <= ngày hiện tại
            if (article.getNgayDang() != null && !article.getNgayDang().after(currentDate)) {
                return articleOpt;
            }
        }
        return Optional.empty();
    }

    // public List<articleEntity> getPublishedArticles() {
    //     List<articleEntity> allArticles = articleRepository.findAll();
    //     Date currentDate = Date.valueOf(LocalDate.now());
        
    //     List<articleEntity> publishedArticles = allArticles.stream()
    //         .filter(article -> article.getNgayDang() != null && 
    //                          !article.getNgayDang().after(currentDate))
    //         .collect(Collectors.toList());
        
    //     logger.info("Retrieved {} published articles (out of {} total)", 
    //                 publishedArticles.size(), allArticles.size());
    //     return publishedArticles;
    // }

    public List<articleEntity> getPublishedArticles() {
        Date currentDate = Date.valueOf(LocalDate.now());
        List<articleEntity> publishedArticles = articleRepository.findPublishedArticles(currentDate);
        logger.info("Retrieved {} published articles from database", publishedArticles.size());
        return publishedArticles;
    }
}