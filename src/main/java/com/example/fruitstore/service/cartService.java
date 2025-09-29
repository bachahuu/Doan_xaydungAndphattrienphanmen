package com.example.fruitstore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.fruitstore.respository.cart.cartRespository;

import jakarta.persistence.criteria.CriteriaBuilder.In;

import com.example.fruitstore.entity.cart.cartDetailEntity;
import com.example.fruitstore.entity.cart.cartEntity;
import com.example.fruitstore.respository.cart.cartDetailRespository;

@Service
public class cartService {
    private final cartRespository cartRespo;
    private final cartDetailRespository cartDetailRespo;

    public cartService(cartRespository cartRespo, cartDetailRespository cartDetailRespo) {
        this.cartRespo = cartRespo;
        this.cartDetailRespo = cartDetailRespo;
    }

    // Lấy tất cả giỏ hàng -> mảng json
    public List<cartEntity> getAllCarts() {
        return cartRespo.findAll();
    }

    // Lấy giỏ hàng theo mã giỏ hàng -> json
    public cartEntity getCartByMaGioHang(String maGioHang) {
        cartEntity cart = cartRespo.findByMaGioHang(maGioHang);
        if (cart == null) {
            throw new RuntimeException("không tìm thấy giỏ hàng: " + maGioHang);
        }
        return cart;
    }

    // Thêm giỏ hàng -> json
    public cartEntity addCart(Integer khachHangId) {
        cartEntity cart = cartRespo.findByKhachHangId(khachHangId);
        if (cart != null) {
            return cart; // Trả về giỏ hàng hiện có nếu đã tồn tại
        }
        cartEntity newCart = new cartEntity();
        newCart.setMaGioHang("GH" + System.currentTimeMillis());
        newCart.setKhachHangId(khachHangId);
        return cartRespo.save(newCart);
    }

    // Xoá giỏ hàng -> string
    public String deleteCart(String maGioHang) {
        cartEntity cart = getCartByMaGioHang(maGioHang);
        cartRespo.delete(cart);
        return "Xoá giỏ hàng thành công!";
    }

    // chi tiết giỏ hàng
    // public cartDetailEntity addCartDetail(Integer gioHangId, cartDetailEntity
    // cartDetail) {
    // cartEntity cart = cartRespo.findById(gioHangId)
    // .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng với ID: " +
    // gioHangId));
    // cartDetailEntity existing =
    // cartDetailRespo.findByGioHangIdAndSanPhamId(gioHangId,
    // cartDetail.getSanPham().getId());
    // if (existing != null) {
    // existing.setSoLuong(existing.getSoLuong() + cartDetail.getSoLuong());
    // return cartDetailRespo.save(existing);
    // } else {
    // cartDetail.setGioHang(cart);
    // return cartDetailRespo.save(cartDetail);
    // }
    // }

    // Xóa sản phẩm khỏi giỏ
    public void removeSanPham(Integer chiTietId) {
        cartDetailRespo.deleteById(chiTietId);
    }
}
