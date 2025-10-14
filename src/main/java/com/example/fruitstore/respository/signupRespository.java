package com.example.fruitstore.respository;

import com.example.fruitstore.entity.loginCustomerEntity;
import com.example.fruitstore.entity.CustomerEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class signupRespository {
	@PersistenceContext
	private EntityManager entityManager;

	public boolean existsByUsername(String username) {
		String jpql = "SELECT COUNT(c) FROM loginCustomerEntity c WHERE c.username = :username";
		Long count = entityManager.createQuery(jpql, Long.class)
				.setParameter("username", username)
				.getSingleResult();
		return count > 0;
	}

	public loginCustomerEntity saveAccount(loginCustomerEntity entity) {
		entityManager.persist(entity);
		return entity;
	}

	public CustomerEntity saveCustomer(CustomerEntity entity) {
		entityManager.persist(entity);
		return entity;
	}
}
