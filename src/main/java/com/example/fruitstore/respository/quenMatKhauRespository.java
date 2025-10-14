package com.example.fruitstore.respository;

import com.example.fruitstore.entity.loginCustomerEntity;
import com.example.fruitstore.entity.CustomerEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class quenMatKhauRespository {
    @PersistenceContext
    private EntityManager entityManager;

    public loginCustomerEntity findAccountByUsername(String username) {
        TypedQuery<loginCustomerEntity> query = entityManager.createQuery(
            "SELECT a FROM loginCustomerEntity a WHERE a.username = :username", loginCustomerEntity.class);
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public CustomerEntity findCustomerByAccountId(int accountId) {
        TypedQuery<CustomerEntity> query = entityManager.createQuery(
            "SELECT c FROM CustomerEntity c WHERE c.taiKhoanId = :accountId", CustomerEntity.class);
        query.setParameter("accountId", accountId);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    @Transactional
    public void updatePassword(int accountId, String newPassword) {
        loginCustomerEntity account = entityManager.find(loginCustomerEntity.class, accountId);
        if (account != null) {
            account.setPassword(newPassword);
            entityManager.merge(account);
        }
    }
}
