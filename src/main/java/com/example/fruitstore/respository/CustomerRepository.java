package com.example.fruitstore.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.fruitstore.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
    CustomerEntity findByTaiKhoanId(Integer taiKhoanId);
}
