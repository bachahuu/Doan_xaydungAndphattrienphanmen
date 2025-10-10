package com.example.fruitstore.respository;

import com.example.fruitstore.entity.discountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface discountRepository extends JpaRepository<discountEntity, Integer> {
    boolean existsByMaKM(String maKM);
    Optional<discountEntity> findByMaKM(String maKM);
}
