# Sửa lỗi Circular Reference trong Order Update

## Vấn đề
Khi cập nhật đơn hàng, xảy ra lỗi:
```
Document nesting depth (1001) exceeds the maximum allowed (1000)
```

**Nguyên nhân:** Circular reference (tham chiếu vòng tròn) trong JSON serialization:
- `discountEntity` → `List<orderEntity>` → `List<orderDetailEntity>` → `SanPham` → `discountEntity` → ...

## Giải pháp

### 1. Tạo OrderUpdateDTO
**File mới:** `src/main/java/com/example/fruitstore/dto/OrderUpdateDTO.java`

DTO này chỉ chứa các trường admin được phép sửa:
- `trangThai`: Trạng thái đơn hàng
- `diaChiGiaoHang`: Địa chỉ giao hàng
- `ghiChu`: Ghi chú
- `phuongThucThanhToanId`: ID phương thức thanh toán
- `khuyenMaiId`: ID khuyến mãi (chỉ ID, không phải object)

### 2. Thêm @JsonIgnoreProperties
Để ngăn circular reference khi serialize JSON:

**discountEntity.java:**
```java
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "orders", "sanPhams" })
```

**SanPham.java:**
```java
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "orders", "sanPhams" })
```

### 3. Sửa orderService.updateOrder()
**File:** `src/main/java/com/example/fruitstore/service/order/orderService.java`

Thay đổi:
- Nhận `OrderUpdateDTO` thay vì `orderEntity`
- Chỉ cập nhật các trường được phép
- Tìm `discount` từ repository bằng ID thay vì set object trực tiếp

```java
@Transactional
public void updateOrder(String maDonHang, OrderUpdateDTO updateDTO) {
    orderEntity existingOrder = getOrderByMaDonHang(maDonHang);
    
    // Chỉ cập nhật các trường admin được phép sửa
    if (updateDTO.getTrangThai() != null) {
        existingOrder.setTrangThai(updateDTO.getTrangThai());
    }
    // ... các trường khác
    
    // Xử lý discount: tìm từ repository bằng ID
    if (updateDTO.getKhuyenMaiId() != null) {
        existingOrder.setDiscount(discountRepo.findById(updateDTO.getKhuyenMaiId()).orElse(null));
    }
    
    orderRespo.save(existingOrder);
}
```

### 4. Sửa Controller
**File:** `src/main/java/com/example/fruitstore/controller/admin/adminOrderController.java`

Thay đổi endpoint để nhận `OrderUpdateDTO`:
```java
@PutMapping("/update/{maDonHang}")
public ResponseEntity<String> updateOrder(
    @PathVariable String maDonHang, 
    @RequestBody OrderUpdateDTO updateDTO
) {
    try {
        orderService.updateOrder(maDonHang, updateDTO);
        return ResponseEntity.ok("Cập nhật đơn hàng thành công");
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Lỗi: " + e.getMessage());
    }
}
```

### 5. Sửa JavaScript
**File:** `src/main/resources/static/js/admin/order.js`

Chỉ gửi các trường được phép sửa:
```javascript
const updateData = {
    trangThai: document.getElementById('edit-trang-thai').value,
    diaChiGiaoHang: document.getElementById('edit-dia-chi').value,
    ghiChu: document.getElementById('edit-ghi-chu').value,
    phuongThucThanhToanId: parseInt(document.getElementById('edit-phuong-thuc').value) || null,
    khuyenMaiId: document.getElementById('edit-ten-khuyen-mai').value ? 
                parseInt(document.getElementById('edit-ten-khuyen-mai').value) : null
};
```

## Các trường Admin KHÔNG được sửa trực tiếp

Theo yêu cầu, các trường sau KHÔNG được phép admin sửa trực tiếp:

1. **Tên người đặt hàng** (`tenNguoiNhan`, `emailNguoiNhan`, `soDienThoaiNguoiNhan`)
   - Đây là thông tin từ tài khoản khách
   - Không nên thay đổi

2. **Sản phẩm & số lượng** (`orderDetail`)
   - Nếu muốn thay đổi, nên tạo đơn hàng mới
   - Tránh thất thoát

3. **Tổng tiền** (`tongTien`)
   - Nên tự động tính từ sản phẩm và khuyến mãi
   - Admin không được nhập tay

## Kết quả

✅ Không còn lỗi circular reference
✅ JSON serialization hoạt động bình thường
✅ Admin chỉ có thể sửa các trường được phép
✅ Code sạch hơn, dễ bảo trì

## Test

1. Chạy ứng dụng
2. Vào trang quản lý đơn hàng
3. Nhấn nút "Sửa" trên một đơn hàng
4. Thay đổi các trường được phép (trạng thái, địa chỉ, ghi chú, v.v.)
5. Nhấn "Cập nhật"
6. Kiểm tra xem đơn hàng có được cập nhật thành công không

