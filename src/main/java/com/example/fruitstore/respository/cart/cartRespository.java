package com.example.fruitstore.respository.cart;

import com.example.fruitstore.entity.cart.cartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface cartRespository extends JpaRepository<cartEntity, Integer> {
    cartEntity findByKhachHangId(Integer khachHangId);
}
