package com.example.fruitstore.respository;

import com.example.fruitstore.entity.accountShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountShopRepository extends JpaRepository<accountShopEntity, Integer> {
    boolean existsByUsername(String username);
}