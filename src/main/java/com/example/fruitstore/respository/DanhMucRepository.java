package com.example.fruitstore.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fruitstore.entity.DanhMuc;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {
	Optional<DanhMuc> findByMaDanhMuc(String maDanhMuc);
}
