package com.example.fruitstore.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.fruitstore.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

}
