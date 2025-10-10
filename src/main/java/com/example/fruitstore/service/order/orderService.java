package com.example.fruitstore.service.order;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.fruitstore.respository.order.orderRespository;

import jakarta.transaction.Transactional;

import com.example.fruitstore.entity.CustomerEntity;
import com.example.fruitstore.entity.order.orderEntity;
import com.example.fruitstore.respository.CustomerRepository;

// import com.example.fruitstore.respository.order.orderDetailRespository; // Sẽ sử dụng sau

@Service
public class orderService {
    private final orderRespository orderRespo;
    private final CustomerRepository customerRepository;
    // private final orderDetailRespository orderDetailRespo; // Sẽ sử dụng sau

    public orderService(orderRespository orderRespo, CustomerRepository customerRepository) {
        this.orderRespo = orderRespo;
        this.customerRepository = customerRepository;
        // this.orderDetailRespo = orderDetailRespo; // Sẽ sử dụng sau
    }

    // lấy tất cả đơn hàng
    public List<orderEntity> getAllOrders() {
        return orderRespo.findAll();
    }

    // Lấy đơn hàng theo mã
    public orderEntity getOrderByMaDonHang(String maDonHang) {
        return orderRespo.findByMaDonHang(maDonHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng: " + maDonHang));
    }

    // Xóa đơn hàng
    // public void deleteOrder(String maDonHang) {
    // orderEntity order = getOrderByMaDonHang(maDonHang);
    // orderRespo.delete(order);
    // }

    // update trạng thái đơn hàng
    // public void updateOrderStatus(String maDonHang, TrangThai trangThai) {
    // orderEntity order = getOrderByMaDonHang(maDonHang);
    // order.setTrangThai(trangThai);
    // orderRespo.save(order);
    // }
    @Transactional
    public void updateOrder(String maDonHang, orderEntity updateOrder) {
        orderEntity existingOrder = getOrderByMaDonHang(maDonHang);
        // Cập nhật các trường cần thiết
        existingOrder.setTrangThai(updateOrder.getTrangThai());
        existingOrder.setDiaChiGiaoHang(updateOrder.getDiaChiGiaoHang());
        existingOrder.setGhiChu(updateOrder.getGhiChu());
        existingOrder.setPhuongThucThanhToanId(updateOrder.getPhuongThucThanhToanId());
        existingOrder.setKhuyenMaiId(updateOrder.getKhuyenMaiId());
        existingOrder.setTongTien(updateOrder.getTongTien());
        orderRespo.save(existingOrder);

    }

}
