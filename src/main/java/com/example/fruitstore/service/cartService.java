package com.example.fruitstore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.fruitstore.respository.cart.cartRespository;

import com.example.fruitstore.entity.cart.cartEntity;
import com.example.fruitstore.entity.SanPham;
import com.example.fruitstore.entity.cart.cartDetailEntity;
import com.example.fruitstore.respository.cart.cartDetailRespository;
import com.example.fruitstore.respository.SanPhamRepository;
import java.util.Date;

@Service
public class cartService {
    private final cartRespository cartRespo;
    private final cartDetailRespository cartDetailRespo;
    private final SanPhamRepository sanPhamRepo;

    public cartService(cartRespository cartRespo, cartDetailRespository cartDetailRespo,
            SanPhamRepository sanPhamRepo) {
        this.cartRespo = cartRespo;
        this.cartDetailRespo = cartDetailRespo;
        this.sanPhamRepo = sanPhamRepo;
    }

    // lấy giỏ hàng theo id khách hàng
    public cartEntity getCartByKhachHangId(Integer khachHangId) {
        return cartRespo.findByKhachHangId(khachHangId);
    }

    // Lấy tất cả giỏ hàng -> mảng json
    public List<cartEntity> getAllCarts() {
        return cartRespo.findAll();
    }

    // Lấy giỏ hàng theo mã giỏ hàng -> json
    // Lấy giỏ hàng theo id -> json
    public cartEntity getCartById(Integer id) {
        return cartRespo.findById(id).orElseThrow(() -> new RuntimeException("không tìm thấy giỏ hàng: " + id));
    }

    // Thêm giỏ hàng -> json
    public cartDetailEntity addCart(Integer khachHangId, Integer productId, Integer quantity) {
        // Tìm giỏ hàng theo khachHangId, nếu không có thì tạo mới
        cartEntity cart = cartRespo.findByKhachHangId(khachHangId);
        if (cart == null) {
            // Nếu không tìm thấy giỏ hàng (cart là null), thì tạo mới
            cart = new cartEntity();
            cart.setKhachHangId(khachHangId);
            cart.setNgayTao(new Date());
            cart = cartRespo.save(cart); // Lưu giỏ hàng mới và gán lại vào biến cart
        }
        // Tìm sản phẩm theo productId xem sanpham đã tồn tại trong giỏ hang chưa
        cartDetailEntity existingDetail = cartDetailRespo.findByCart_IdAndSanPham_Id(cart.getId(), productId);

        if (existingDetail != null) {
            existingDetail.setSoLuong(existingDetail.getSoLuong() + quantity);
            return cartDetailRespo.save(existingDetail);
        } else {
            cartDetailEntity newDetail = new cartDetailEntity();
            SanPham product = sanPhamRepo.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + productId));

            newDetail.setCart(cart);
            newDetail.setSanPham(product);
            newDetail.setSoLuong(quantity);
            return cartDetailRespo.save(newDetail);
        }
    }

    // update số lượng sản phẩn trong giỏ hàng
    public Double updateSoLuongSanPham(Integer chiTietId, Integer soLuong) {
        cartDetailEntity cartDetail = cartDetailRespo.findById(chiTietId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết giỏ hàng với ID: " + chiTietId));

        if (soLuong <= 0) {
            return -1.0; // Trả về -1 để frontend hiểu là cần hỏi xoá
        }
        cartDetail.setSoLuong(soLuong);
        cartDetailRespo.save(cartDetail);

        // tính lại tiền trong giỏ hàng
        Double total = cartDetail.getSanPham().getGia() * soLuong;
        return total;
    }

    // Xoá giỏ hàng theo id -> string
    public String deleteCart(Integer id) {
        cartEntity cart = getCartById(id);
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
