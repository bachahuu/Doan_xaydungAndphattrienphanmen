package com.example.fruitstore.respository.order;

import org.springframework.stereotype.Repository;
import com.example.fruitstore.entity.order.orderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface orderRespository extends JpaRepository<orderEntity, Integer> {
    Optional<orderEntity> findByMaDonHang(String maDonHang);

}
