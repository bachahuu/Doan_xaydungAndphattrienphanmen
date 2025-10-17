package com.example.fruitstore.respository;

import com.example.fruitstore.entity.articleEntity;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface articleRepository extends JpaRepository<articleEntity, Integer> {
    @Query("SELECT a FROM articleEntity a WHERE a.ngayDang <= :currentDate ORDER BY a.ngayDang DESC")
    List<articleEntity> findPublishedArticles(@Param("currentDate") Date currentDate);
}