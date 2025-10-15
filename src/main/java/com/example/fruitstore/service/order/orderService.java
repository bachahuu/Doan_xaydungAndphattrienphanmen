package com.example.fruitstore.service.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.fruitstore.respository.order.orderRespository;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import com.example.fruitstore.entity.CustomerEntity;
import com.example.fruitstore.entity.cart.cartEntity;
import com.example.fruitstore.entity.cart.cartDetailEntity;
import com.example.fruitstore.entity.discountEntity;
import com.example.fruitstore.entity.phuongThucThanhToan;
import com.example.fruitstore.entity.order.orderDetailEntity;
import com.example.fruitstore.entity.order.orderEntity;
import com.example.fruitstore.respository.CustomerRepository;
import com.example.fruitstore.respository.discountRepository;
import com.example.fruitstore.respository.phuongThucThanhToanRepository;
import com.example.fruitstore.service.cartService;
import com.example.fruitstore.dto.OrderUpdateDTO;
import java.util.Date;
import java.util.Map;

// import com.example.fruitstore.respository.order.orderDetailRespository; // Sẽ sử dụng sau

@Service
public class orderService {
    private final orderRespository orderRespo;
    private final CustomerRepository customerRepository;
    private final cartService cartService;
    private final discountRepository discountRepo;
    private final phuongThucThanhToanRepository phuongThucThanhToanRepo;
    // private final orderDetailRespository orderDetailRespo; // Sẽ sử dụng sau

    public orderService(orderRespository orderRespo, CustomerRepository customerRepository,
            cartService cartService, discountRepository discountRepo,
            phuongThucThanhToanRepository phuongThucThanhToanRepo) {
        this.orderRespo = orderRespo;
        this.customerRepository = customerRepository;
        this.cartService = cartService;
        this.discountRepo = discountRepo;
        this.phuongThucThanhToanRepo = phuongThucThanhToanRepo;
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
    public void updateOrder(String maDonHang, orderEntity updatedOrderData) {
        orderEntity existingOrder = getOrderByMaDonHang(maDonHang);

        // 2. Cập nhật các trường được phép sửa từ dữ liệu client gửi lên
        existingOrder.setTrangThai(updatedOrderData.getTrangThai());
        existingOrder.setDiaChiGiaoHang(updatedOrderData.getDiaChiGiaoHang());
        existingOrder.setGhiChu(updatedOrderData.getGhiChu());
        existingOrder.setSoDienThoaiNguoiNhan(updatedOrderData.getSoDienThoaiNguoiNhan());
        existingOrder.setTenNguoiNhan(updatedOrderData.getTenNguoiNhan());
        existingOrder.setGhiChu(updatedOrderData.getGhiChu());
        if (updatedOrderData.getPhuongThucThanhToan() != null
                && updatedOrderData.getPhuongThucThanhToan().getId() != null) {
            phuongThucThanhToan pttt = phuongThucThanhToanRepo
                    .findById(updatedOrderData.getPhuongThucThanhToan().getId())
                    .orElse(null);
            existingOrder.setPhuongThucThanhToan(pttt);
        } else {
            existingOrder.setPhuongThucThanhToan(null);
        }

        // Xử lý discount: tìm từ repository bằng ID để tránh circular reference
        if (updatedOrderData.getDiscount() != null && updatedOrderData.getDiscount().getId() != null) {
            discountEntity discount = discountRepo.findById(updatedOrderData.getDiscount().getId())
                    .orElse(null);
            existingOrder.setDiscount(discount);
        } else {
            existingOrder.setDiscount(null);
        }

        orderRespo.save(existingOrder);
    }

    @Transactional
    public orderEntity createOrder(Integer userId, Map<String, String> shippingInfo, Integer paymentMethodId,
            Integer discountId, HttpSession session) {

        // BƯỚC 1: LẤY GIỎ HÀNG VÀ KIỂM TRA
        cartEntity cart = cartService.getCartByKhachHangId(userId);
        if (cart == null || cart.getCartDetail() == null || cart.getCartDetail().isEmpty()) {
            throw new IllegalStateException("Giỏ hàng của bạn đang trống.");
        }

        // 2. Tạo đối tượng Order và set các thông tin cơ bản
        orderEntity order = new orderEntity();
        order.setMaDonHang("DH" + System.currentTimeMillis());
        order.setNgayTao(new Date());
        order.setTrangThai(orderEntity.TrangThai.ChoXuLy);
        order.setTenNguoiNhan(shippingInfo.getOrDefault("txtname", ""));
        order.setSoDienThoaiNguoiNhan(shippingInfo.getOrDefault("txtphone", ""));
        order.setDiaChiGiaoHang(shippingInfo.getOrDefault("txtaddress", ""));
        order.setGhiChu(shippingInfo.getOrDefault("orderNote", "")); // Lấy ghi chú đơn hàng

        // 3. Set tổng tiền
        String finalTotalStr = shippingInfo.getOrDefault("finalTotal", "0");
        BigDecimal finalTotal = new BigDecimal(finalTotalStr.replaceAll("[^0-9\\.-]", ""));
        order.setTongTien(finalTotal);

        // 4. Set phương thức thanh toán
        phuongThucThanhToan paymentMethod = phuongThucThanhToanRepo.findById(paymentMethodId).orElse(null);
        order.setPhuongThucThanhToan(paymentMethod);

        // 5. Set discount/khuyến mãi (nếu có)
        if (discountId != null) {
            discountEntity discount = discountRepo.findById(discountId).orElse(null);
            if (discount != null) {
                order.setDiscount(discount);
            }
        }

        // 6. Liên kết với khách hàng
        CustomerEntity customer = customerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + userId));
        order.setKhachHang(customer);
        // 7. Tạo các chi tiết đơn hàng từ giỏ hàng
        List<orderDetailEntity> details = new ArrayList<>();
        for (cartDetailEntity cd : cart.getCartDetail()) {
            orderDetailEntity od = new orderDetailEntity();
            od.setSanPham(cd.getSanPham());
            od.setSoLuong(cd.getSoLuong());
            Double prodPrice = cd.getSanPham().getGia();
            od.setGia(prodPrice != null ? BigDecimal.valueOf(prodPrice) : BigDecimal.ZERO);
            od.setOrder(order); // Liên kết chi tiết với đơn hàng chính
            details.add(od);
        }
        order.setOrderDetail(details);

        // 8. Lưu đơn hàng vào database (bao gồm cả các chi tiết nhờ CascadeType.ALL)
        orderEntity savedOrder = orderRespo.save(order);

        // 9. Xóa giỏ hàng và thông tin session sau khi đã đặt hàng thành công
        cartService.deleteCart(cart.getId());
        session.removeAttribute("shippingInfo");

        return savedOrder;

    }

}
