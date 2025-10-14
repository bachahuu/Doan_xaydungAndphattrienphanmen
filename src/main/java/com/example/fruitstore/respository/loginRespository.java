package com.example.fruitstore.respository;

import com.example.fruitstore.entity.loginCustomerEntity;
import com.example.fruitstore.entity.loginShopEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class loginRespository {
	@PersistenceContext
	private EntityManager entityManager;

	public loginCustomerEntity findCustomerByUsername(String username) {
		String jpql = "SELECT c FROM loginCustomerEntity c WHERE c.username = :username";
		TypedQuery<loginCustomerEntity> query = entityManager.createQuery(jpql, loginCustomerEntity.class);
		query.setParameter("username", username);
		return query.getResultList().stream().findFirst().orElse(null);
	}

	public loginShopEntity findShopByUsername(String username) {
		String jpql = "SELECT s FROM loginShopEntity s WHERE s.username = :username";
		TypedQuery<loginShopEntity> query = entityManager.createQuery(jpql, loginShopEntity.class);
		query.setParameter("username", username);
		return query.getResultList().stream().findFirst().orElse(null);
	}
}
