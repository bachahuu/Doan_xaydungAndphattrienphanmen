package com.example.fruitstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.fruitstore.entity.loginShopEntity;
import com.example.fruitstore.entity.loginCustomerEntity;
import com.example.fruitstore.respository.loginRespository;

@Service
public class loginService {
	@Autowired
	private loginRespository loginRespository;

	public Object authenticate(String username, String password) {
		// Check shop account first
		loginShopEntity shop = loginRespository.findShopByUsername(username);
		if (shop != null && shop.getPassword().equals(password)) {
			return shop;
		}
		// Check customer account
		loginCustomerEntity customer = loginRespository.findCustomerByUsername(username);
		if (customer != null && customer.getPassword().equals(password)) {
			return customer;
		}
		return null;
	}
}
