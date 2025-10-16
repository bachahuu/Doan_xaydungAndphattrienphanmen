# Sửa quan hệ JPA giữa Order và Phương thức thanh toán

## 🔍 Vấn đề

Hai bảng `donhang` và `thanhtoan` có **foreign key** trong database nhưng trong entity `orderEntity` chỉ lưu **ID** mà không có **quan hệ JPA**.

### Trước khi sửa:
```java
@Column(name = "phuongThucThanhToanId")
private Integer phuongThucThanhToanId;  // ❌ Chỉ lưu ID
```

### Hậu quả:
- ❌ Không thể truy cập thông tin phương thức thanh toán (tên, mô tả)
- ❌ Phải query riêng để lấy thông tin
- ❌ Không tận dụng được lazy loading của JPA
- ❌ Code không nhất quán với các quan hệ khác (Customer, Discount)

## 🛠️ Giải pháp

### 1. **Entity** (`orderEntity.java`)

**Thêm quan hệ ManyToOne:**
```java
// mỗi đơn hàng gắn với 1 phương thức thanh toán
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "phuongThucThanhToanId")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
private phuongThucThanhToan phuongThucThanhToan;
```

**Import thêm:**
```java
import com.example.fruitstore.entity.phuongThucThanhToan;
```

### 2. **Service** (`orderService.java`)

**Thêm repository:**
```java
private final phuongThucThanhToanRepository phuongThucThanhToanRepo;

public orderService(..., phuongThucThanhToanRepository phuongThucThanhToanRepo) {
    this.phuongThucThanhToanRepo = phuongThucThanhToanRepo;
}
```

**Cập nhật method `createOrder()`:**
```java
// 4. Set phương thức thanh toán
phuongThucThanhToan paymentMethod = phuongThucThanhToanRepo.findById(paymentMethodId).orElse(null);
order.setPhuongThucThanhToan(paymentMethod);
```

**Cập nhật method `updateOrder()`:**
```java
if (updateDTO.getPhuongThucThanhToanId() != null) {
    existingOrder.setPhuongThucThanhToan(
        phuongThucThanhToanRepo.findById(updateDTO.getPhuongThucThanhToanId()).orElse(null)
    );
}
```

### 3. **JavaScript** (`order.js`)

**Hiển thị tên phương thức thanh toán thay vì ID:**

```javascript
// Chi tiết đơn hàng
setText('detail-phuong-thuc', order.phuongThucThanhToan ? order.phuongThucThanhToan.name : '-');

// Form sửa
document.getElementById('edit-phuong-thuc').value = order.phuongThucThanhToan ? order.phuongThucThanhToan.id : '';
```

## ✅ Kết quả

### Ưu điểm:
- ✅ Có quan hệ JPA đúng với database
- ✅ Có thể truy cập `order.getPhuongThucThanhToan().getName()`
- ✅ Lazy loading tự động
- ✅ Nhất quán với các quan hệ khác (Customer, Discount)
- ✅ Hiển thị tên phương thức thanh toán thay vì ID
- ✅ JSON serialize đúng với `@JsonIgnoreProperties`

### So sánh:

| Trước | Sau |
|-------|-----|
| `order.phuongThucThanhToanId` (Integer) | `order.phuongThucThanhToan` (Object) |
| Hiển thị: `1`, `2`, `3` | Hiển thị: "Tiền mặt", "Chuyển khoản" |
| Phải query riêng để lấy tên | Lazy load tự động |
| Không có quan hệ JPA | Có quan hệ JPA đúng chuẩn |

## 📋 Cấu trúc quan hệ hiện tại

```
orderEntity
├── CustomerEntity (khachHang) - ManyToOne ✅
├── phuongThucThanhToan - ManyToOne ✅ (MỚI)
├── discountEntity (discount) - ManyToOne ✅
└── List<orderDetailEntity> (orderDetail) - OneToMany ✅
```

## 🧪 Test

1. **Khởi động lại ứng dụng**
2. **Kiểm tra chi tiết đơn hàng:**
   - Vào quản lý đơn hàng
   - Nhấn "Xem chi tiết"
   - Trường "Phương thức thanh toán" → Hiển thị **tên** (ví dụ: "Tiền mặt", "Chuyển khoản")
3. **Kiểm tra sửa đơn hàng:**
   - Nhấn "Sửa"
   - Trường "Phương thức thanh toán" → Hiển thị **ID** để chọn
4. **Kiểm tra tạo đơn hàng mới:**
   - Tạo đơn hàng mới
   - Kiểm tra database → `phuongThucThanhToanId` có giá trị đúng

## 📝 Lưu ý

- Foreign key trong database vẫn hoạt động bình thường
- `@JsonIgnoreProperties` ngăn circular reference khi serialize JSON
- `fetch = FetchType.LAZY` để tối ưu performance (chỉ load khi cần)
- Nếu phương thức thanh toán bị xóa, `phuongThucThanhToan` sẽ là `null`


