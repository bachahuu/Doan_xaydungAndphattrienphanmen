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
import com.example.fruitstore.entity.order.orderEntity.TrangThai;
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

        // Bước 1: Gọi phương thức helper để xây dựng đối tượng order
        orderEntity order = buildOrderFromCart(userId, shippingInfo, paymentMethodId, discountId, TrangThai.ChoXuLy);

        // Bước 2: Lưu đơn hàng vào database
        orderEntity savedOrder = orderRespo.save(order);

        // Bước 3: Xóa giỏ hàng và thông tin session sau khi đã đặt hàng thành công
        cartEntity cart = cartService.getCartByKhachHangId(userId);
        if (cart != null) {
            cartService.deleteCart(cart.getId());
        }
        session.removeAttribute("shippingInfo");

        return savedOrder;
    }

    // Phương thức để tạo đơn hàng chờ thanh toán (dành cho MoMo) , không xóa giỏ
    // hàng ngay
    @Transactional
    public orderEntity createPendingOrder(Integer userId, Map<String, String> shippingInfo, Integer paymentMethodId,
            Integer discountId) {

        // Chỉ cần gọi helper để xây dựng và lưu lại là xong.
        orderEntity order = buildOrderFromCart(userId, shippingInfo, paymentMethodId, discountId,
                TrangThai.CHO_THANH_TOAN);
        return orderRespo.save(order);
    }

    private orderEntity buildOrderFromCart(Integer userId, Map<String, String> shippingInfo, Integer paymentMethodId,
            Integer discountId, TrangThai initialStatus) {
        // 1. LẤY GIỎ HÀNG VÀ KIỂM TRA
        cartEntity cart = cartService.getCartByKhachHangId(userId);
        if (cart == null || cart.getCartDetail() == null || cart.getCartDetail().isEmpty()) {
            throw new IllegalStateException("Giỏ hàng của bạn đang trống.");
        }

        // 2. TẠO ĐỐI TƯỢNG ORDER VÀ SET THÔNG TIN CƠ BẢN
        orderEntity order = new orderEntity();
        order.setMaDonHang("DH" + System.currentTimeMillis());
        order.setNgayTao(LocalDateTime.now());
        order.setTrangThai(initialStatus); // Sử dụng trạng thái được truyền vào

        // 3. SET THÔNG TIN GIAO HÀNG
        order.setTenNguoiNhan(shippingInfo.getOrDefault("txtname", ""));
        order.setSoDienThoaiNguoiNhan(shippingInfo.getOrDefault("txtphone", ""));
        order.setDiaChiGiaoHang(shippingInfo.getOrDefault("txtaddress", ""));
        order.setGhiChu(shippingInfo.getOrDefault("orderNote", ""));

        // 4. SET PHÍ SHIP VÀ TỔNG TIỀN
        String shippingFeeStr = shippingInfo.getOrDefault("shippingFee", "30000");
        BigDecimal shippingFee = new BigDecimal(shippingFeeStr.replaceAll("[^0-9\\.-]", ""));
        order.setPhiShip(shippingFee);

        String finalTotalStr = shippingInfo.getOrDefault("finalTotal", "0");
        BigDecimal finalTotal = new BigDecimal(finalTotalStr.replaceAll("[^0-9\\.-]", ""));
        order.setTongTien(finalTotal);

        // 5. SET PHƯƠNG THỨC THANH TOÁN
        phuongThucThanhToan paymentMethod = phuongThucThanhToanRepo.findById(paymentMethodId)
                .orElseThrow(() -> new RuntimeException("Phương thức thanh toán không hợp lệ."));
        order.setPhuongThucThanhToan(paymentMethod);

        // 6. SET MÃ GIẢM GIÁ (NẾU CÓ)
        if (discountId != null) {
            discountRepo.findById(discountId).ifPresent(order::setDiscount);
        }

        // 7. LIÊN KẾT VỚI KHÁCH HÀNG
        CustomerEntity customer = customerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + userId));
        order.setKhachHang(customer);

        // 8. TẠO CHI TIẾT ĐƠN HÀNG TỪ GIỎ HÀNG
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

        return order;
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

    @Transactional
    public void processSuccessfulOnlinePayment(Integer orderId, Integer userId) {
        orderEntity order = getOrderById(orderId);

        if (order != null && order.getTrangThai() == orderEntity.TrangThai.CHO_THANH_TOAN) {
            // 1. Cập nhật trạng thái thành "Chờ xử lý"
            order.setTrangThai(orderEntity.TrangThai.ChoXuLy);
            orderRespo.save(order);

            // 2. Xóa giỏ hàng của khách hàng
            cartEntity cart = cartService.getCartByKhachHangId(userId);
            if (cart != null) {
                cartService.deleteCart(cart.getId());
            }
        }
    }

}
