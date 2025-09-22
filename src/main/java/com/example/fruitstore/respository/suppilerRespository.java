package com.example.fruitstore.respository;

import com.example.fruitstore.entity.supplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface suppilerRespository extends JpaRepository<supplierEntity, Integer> {
    Optional<supplierEntity> findByMaNCC(String maNCC);
}
