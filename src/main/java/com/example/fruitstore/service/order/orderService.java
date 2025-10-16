package com.example.fruitstore.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public List<orderEntity> getTodaysOrders() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay().minusNanos(1);

        return orderRespo.findByNgayTaoBetween(startOfDay, endOfDay);
    }

    // Lấy đơn hàng theo ID khách hàng
    public List<orderEntity> find(Integer customerId) {
        return orderRespo.findByKhachHangId(customerId);
    }

    // lấy đơn hàng theo ID đơn hàng
    public orderEntity getOrderById(Integer orderId) {
        return orderRespo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId));
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
        order.setNgayTao(LocalDateTime.now());
        order.setTrangThai(orderEntity.TrangThai.ChoXuLy);
        order.setTenNguoiNhan(shippingInfo.getOrDefault("txtname", ""));
        order.setSoDienThoaiNguoiNhan(shippingInfo.getOrDefault("txtphone", ""));
        order.setDiaChiGiaoHang(shippingInfo.getOrDefault("txtaddress", ""));
        order.setGhiChu(shippingInfo.getOrDefault("orderNote", "")); // Lấy ghi chú đơn hàng

        // THÊM LOGIC LƯU PHÍ SHIP TỪ SESSION
        String shippingFeeStr = shippingInfo.getOrDefault("shippingFee", "30000"); // Lấy phí ship
        BigDecimal shippingFee = new BigDecimal(shippingFeeStr.replaceAll("[^0-9\\.-]", ""));
        order.setPhiShip(shippingFee); // Gán vào đối tượng order

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

    @Transactional
    public void cancelOrder(Integer orderId, Integer customerId) {
        orderEntity order = getOrderById(orderId);
        // 2. KIỂM TRA: Đảm bảo đơn hàng này thuộc về đúng khách hàng.
        if (!order.getKhachHang().getId().equals(customerId)) {
            throw new SecurityException("Bạn không có quyền hủy đơn hàng này.");
        }

        // 3. KIỂM TRA: Đảm bảo chỉ được hủy khi trạng thái là "Chờ xử lý".
        if (order.getTrangThai() != orderEntity.TrangThai.ChoXuLy) {
            throw new IllegalStateException("Không thể hủy khi đơn hàng đã được xử lý.");
        }

        // 4. CẬP NHẬT TRẠNG THÁI: Đây là dòng quan trọng nhất.
        // Nó chỉ thay đổi giá trị của cột 'trangThai'.
        order.setTrangThai(orderEntity.TrangThai.DaHuy);

        // 5. LƯU THAY ĐỔI: Phương thức .save() khi được gọi trên một đối tượng đã tồn
        // tại
        // sẽ thực hiện một câu lệnh SQL UPDATE, không phải DELETE.
        orderRespo.save(order);
    }

}
