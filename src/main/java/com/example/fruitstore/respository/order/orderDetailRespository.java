package com.example.fruitstore.respository.order;

import org.springframework.stereotype.Repository;
import com.example.fruitstore.entity.order.orderDetailEntity;
import com.example.fruitstore.entity.order.orderEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface orderDetailRespository extends JpaRepository<orderDetailEntity, Integer> {
    List<orderDetailEntity> findByOrder(orderEntity order);
}
