package com.example.fruitstore.respository.cart;

import com.example.fruitstore.entity.cart.cartDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface cartDetailRespository extends JpaRepository<cartDetailEntity, Integer> {
    // use nested property path: cart.id and sanPham.id
    cartDetailEntity findByCart_IdAndSanPham_Id(Integer gioHangId, Integer sanPhamId);

    List<cartDetailEntity> findByCart_Id(Integer gioHangId);

}
