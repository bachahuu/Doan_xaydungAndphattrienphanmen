document.addEventListener("DOMContentLoaded", function () {
    const checkbox = document.getElementById("ckbstore");
    const storeinfomation = document.getElementById("storeInfomation");
    const infomation_an = document.getElementById("info_an");
    const infomation_an1 = document.getElementById("info_an1");
    const infomation_an2 = document.getElementById("info_an2");
  
    //sử lý sự kiện
    checkbox.addEventListener("change", function () {
      if (checkbox.checked) {
        storeinfomation.style.display = "flex";
        infomation_an.style.display = "none";
        infomation_an1.style.display = "none";
        infomation_an2.style.display = "none";
      } else {
        storeinfomation.style.display = "none";
        infomation_an.style.display = "flex";
        infomation_an1.style.display = "flex";
        infomation_an2.style.display = "flex";
      }
    });

    updateCartTotalFromServer(); // Cập nhật tổng tiền ban đầu khi trang load xong

    // Xử lý sự kiện khi chọn mã khuyến mãi
    const applybutton = document.getElementById('apply-voucher-btn');
    if(applybutton){
      applybutton.addEventListener('click', function(e) {
          e.preventDefault();
          applyDiscount();
      });
    }



});
  
function pay() {window.location.href = "/payment";}

// Hàm tiện ích để định dạng tiền tệ
function formatCurrency(number) {
    return new Intl.NumberFormat('vi-VN').format(Math.round(number));
}
// Lấy tổng tiền giỏ hàng từ input ẩn mà server đã truyền vào
function updateCartTotalFromServer() {
  const cartTotal = parseFloat(document.getElementById('cartTotalPriceInput').value) || 0;

  const tamtinhEl = document.getElementById('tamtinh-value');
  const totalEl = document.getElementById('total-value');
  const shippingFee = 30000; // Phí vận chuyển cố định

  if (tamtinhEl) tamtinhEl.textContent = formatCurrency(cartTotal);
  const finalTotal = cartTotal + shippingFee;
  if (totalEl) totalEl.textContent = formatCurrency(cartTotal + shippingFee);
  // Cập nhật giá trị cuối cùng vào input ẩn để gửi về server
    document.getElementById('finalTotalInput').value = finalTotal;
}

// hàm xử lý nút áp dụng mã khuyến mãi
function applyDiscount() {
  const discountselect = document.getElementById('voucher');
  const selectedOption = discountselect.options[discountselect.selectedIndex];
  const cartTotal = parseFloat(document.getElementById('cartTotalPriceInput').value) || 0;

  const shippingFee = 30000;
  let discountAmount = 0;
  let discountId = ""; // Biến để lưu ID của voucher
  if (selectedOption && selectedOption.value) {
          // Lấy giá trị giảm giá và giá trị điều kiện từ thẻ option
          const fixedDiscountAmount = parseFloat(selectedOption.getAttribute('data-value')) || 0;
          const conditionValue = parseFloat(selectedOption.getAttribute('data-condition-value')) || 0;

          // Kiểm tra điều kiện đơn hàng
          if (cartTotal >= conditionValue) {
              // TH1: Đơn hàng ĐỦ điều kiện -> Gán giá trị giảm giá
              if (!isNaN(fixedDiscountAmount)) {
                  discountAmount = fixedDiscountAmount;
                  discountId = selectedOption.value; // Lưu ID của voucher
              }
          } else {
              // TH2: Đơn hàng KHÔNG đủ điều kiện -> Thông báo lỗi và reset dropdown
              const requiredAmountFormatted = formatCurrency(conditionValue);
              alert(`Mã này chỉ áp dụng cho đơn hàng từ ${requiredAmountFormatted}đ trở lên.`);
              
              // Tự động trả dropdown về lựa chọn mặc định để người dùng biết mã đã không được áp dụng
              discountselect.value = ""; 
          }
  }

  // Đảm bảo số tiền giảm không lớn hơn tổng tiền hàng
    if (discountAmount > cartTotal) {
        discountAmount = cartTotal;
    }
    const finalTotalEl = cartTotal - discountAmount + shippingFee ;

    // Cập nhật giao diện
    document.getElementById('giamgia-value').innerText = formatCurrency(discountAmount);
    document.getElementById('total-value').innerText = formatCurrency(finalTotalEl);

    // CẬP NHẬT CÁC INPUT ẨN ĐỂ GỬI LÊN SERVER
    document.getElementById('discountIdInput').value = discountId;
    document.getElementById('finalTotalInput').value = finalTotalEl;
  
}