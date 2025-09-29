package com.example.fruitstore.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.fruitstore.entity.CustomerEntity;
import com.example.fruitstore.respository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }
}
