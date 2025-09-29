package com.example.fruitstore.respository.order;

import org.springframework.stereotype.Repository;
import com.example.fruitstore.entity.order.orderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface orderDetailRespository extends JpaRepository<orderDetailEntity, Integer> {

}
