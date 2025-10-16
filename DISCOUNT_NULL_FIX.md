# Sửa lỗi khuyenMaiId = NULL khi tạo đơn hàng

## 🔍 Vấn đề

Khi tạo đơn hàng mới với mã giảm giá, `khuyenMaiId` trong database vẫn là **NULL** mặc dù user đã chọn voucher.

## 🔎 Nguyên nhân

**Luồng dữ liệu bị đứt ở bước cuối:**

1. ✅ **Location page**: User chọn voucher → `location.js` lưu vào `discountIdInput`
2. ✅ **Location form submit**: Gửi `discountId` lên server → Lưu vào session
3. ✅ **Payment page**: Hiển thị thông tin từ session
4. ❌ **Payment form submit**: **THIẾU input `discountId`** → Không gửi lên server
5. ❌ **Controller**: Không nhận được `discountId` → Không lưu vào database

## 🛠️ Giải pháp

### Thêm input hidden `discountId` vào form checkout

**File:** `src/main/resources/templates/user/products/payment.html`

```html
<form id="checkoutForm" th:action="@{/orders/checkout}" method="post">
    <!-- Lấy discountId từ session -->
    <input type="hidden" name="discountId" th:value="${shippingInfo?.get('discountId') ?: ''}" />
    <button class="btn btn-primary" id="complete_pay" type="button">Xác nhận thanh toán</button>
    <a class="btn btn-secondary ms-2" th:href="@{/home}">Home</a>
</form>
```

## 📋 Luồng hoạt động sau khi sửa

```
1. User chọn voucher ở trang Location
   ↓
2. location.js lưu discountId vào input discountIdInput
   ↓
3. Form submit → Controller lưu vào session (shippingInfo)
   ↓
4. Redirect sang trang Payment
   ↓
5. Payment.html hiển thị form với input hidden discountId từ session
   ↓
6. User submit form checkout
   ↓
7. Controller nhận discountId từ params
   ↓
8. Service lưu discountId vào database
   ↓
9. ✅ khuyenMaiId có giá trị trong database
```

## ✅ Kết quả

- ✅ Form checkout có input `discountId`
- ✅ Giá trị `discountId` được lấy từ session
- ✅ Controller nhận được `discountId`
- ✅ Service lưu `discountId` vào database
- ✅ `khuyenMaiId` có giá trị trong database

## 🧪 Test

1. **Khởi động lại ứng dụng**
2. **Tạo đơn hàng với voucher:**
   - Vào trang giỏ hàng
   - Nhấn "Thanh toán"
   - Nhập thông tin giao hàng
   - **Chọn voucher/khuyến mãi**
   - Nhấn "Hoàn tất thông tin"
   - Chọn phương thức thanh toán
   - Nhấn "Xác nhận thanh toán"
3. **Kiểm tra database:**
   - Mở phpMyAdmin
   - Vào bảng `donhang`
   - Tìm đơn hàng vừa tạo
   - Kiểm tra cột `khuyenMaiId` → **Phải có giá trị** (ví dụ: 1, 2, ...)

## 📝 Lưu ý

- Nếu không chọn voucher, `discountId` sẽ là empty string → Controller xử lý thành `null` → `khuyenMaiId` = NULL (đúng)
- Nếu chọn voucher, `discountId` có giá trị → Controller xử lý thành Integer → `khuyenMaiId` có giá trị (đúng)


