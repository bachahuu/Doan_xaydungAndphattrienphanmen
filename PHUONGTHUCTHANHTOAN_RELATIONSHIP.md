# Thêm quan hệ JPA cho phuongThucThanhToan

## 🔍 Vấn đề

Entity `phuongThucThanhToan` chưa có quan hệ ngược lại với `Order`, không nhất quán với các entity khác.

## 🛠️ Đã sửa

### File: `phuongThucThanhToan.java`

**Thêm quan hệ OneToMany ngược lại với Order:**

```java
// Quan hệ ngược lại với Order
@OneToMany(mappedBy = "phuongThucThanhToan", fetch = FetchType.LAZY)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
private List<orderEntity> orders;
```

**Import thêm:**
```java
import java.util.List;
import com.example.fruitstore.entity.order.orderEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
```

**Thêm annotation ở class level:**
```java
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "orders" })
```

## ✅ Kết quả

### Ưu điểm:
- ✅ Có quan hệ JPA đầy đủ (bidirectional)
- ✅ Có thể truy cập `paymentMethod.getOrders()` để lấy danh sách đơn hàng
- ✅ Nhất quán với các entity khác (discountEntity có `orders`, `sanPhams`)
- ✅ `@JsonIgnoreProperties` ngăn circular reference khi serialize JSON
- ✅ `fetch = FetchType.LAZY` tối ưu performance

### Cấu trúc quan hệ hoàn chỉnh:

```
phuongThucThanhToan (1) ←→ (N) orderEntity
```

**Chiều thuận (Order → PaymentMethod):**
```java
order.getPhuongThucThanhToan().getName(); // "Tiền mặt"
```

**Chiều ngược (PaymentMethod → Orders):**
```java
paymentMethod.getOrders(); // List<orderEntity>
```

## 📋 So sánh với các entity khác

| Entity | Quan hệ với Order | Ngược lại |
|--------|-------------------|-----------|
| `CustomerEntity` | ManyToOne ✅ | OneToMany ✅ (có sẵn) |
| `discountEntity` | ManyToOne ✅ | OneToMany ✅ (có sẵn) |
| `phuongThucThanhToan` | ManyToOne ✅ | OneToMany ✅ **(VỪA THÊM)** |
| `SanPham` | N/A | N/A (qua orderDetail) |

## 🧪 Test

1. **Khởi động lại ứng dụng**
2. **Kiểm tra không có lỗi**
3. **Có thể sử dụng:**
   ```java
   // Lấy phương thức thanh toán
   phuongThucThanhToan payment = paymentRepo.findById(1).orElse(null);
   
   // Lấy danh sách đơn hàng sử dụng phương thức này
   List<orderEntity> orders = payment.getOrders();
   ```

## 📝 Lưu ý

- `@JsonIgnoreProperties` ở class level để tránh serialize `orders` khi không cần thiết
- `@JsonIgnoreProperties` ở field level để tránh circular reference
- `fetch = FetchType.LAZY` để chỉ load khi cần (tối ưu performance)
- Quan hệ này không bắt buộc nhưng giúp code nhất quán và dễ sử dụng


