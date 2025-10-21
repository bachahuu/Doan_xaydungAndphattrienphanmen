document.addEventListener("DOMContentLoaded", function () {
    const checkbox = document.getElementById("ckbstore");
    const storeinfomation = document.getElementById("storeInfomation");
    const infomation_an = document.getElementById("info_an");
    const infomation_an1 = document.getElementById("info_an1");
    
    const addressInput = document.getElementById("address"); 
    const storeAddressValue = "Số 15 Nguyễn Khuyến, Văn Quán, Hà Đông, Hà Nội";

    // MỚI: Lấy phần tử hiển thị phí vận chuyển bằng ID
    const shippingFeeDisplay = document.getElementById("shipping-fee-value");

    //sử lý sự kiện thay đổi của checkbox
    checkbox.addEventListener("change", function () {
      if (checkbox.checked) {
        storeinfomation.style.display = "flex";
        infomation_an.style.display = "none";
        infomation_an1.style.display = "none";
        addressInput.value = storeAddressValue;
        
        // Bỏ logic ẩn cả dòng đi
        // if(shippingFeeRow) shippingFeeRow.style.display = 'none';

      } else {
        storeinfomation.style.display = "none";
        infomation_an.style.display = "flex";
        infomation_an1.style.display = "flex";
        addressInput.value = "";
        
        // Bỏ logic hiện cả dòng đi
        // if(shippingFeeRow) shippingFeeRow.style.display = 'flex';
      }
      
      // Tính lại toàn bộ tổng tiền (hàm này sẽ tự động cập nhật phí ship)
      applyDiscount();
    });

    updateCartTotalFromServer();

    const applybutton = document.getElementById('apply-voucher-btn');
    if(applybutton){
      applybutton.addEventListener('click', function(e) {
          e.preventDefault();
          applyDiscount();
      });
    }

    // --- LOGIC VALIDATE FORM MỚI THÊM VÀO ---
    const shippingForm = document.getElementById("shippingForm");
    if (shippingForm) {
        shippingForm.addEventListener("submit", function (event) {
            // Lấy giá trị từ các trường input
            const name = document.getElementById("name").value.trim();
            const phone = document.getElementById("phone").value.trim();
            const address = document.getElementById("address").value.trim();
            const isPickup = document.getElementById("ckbstore").checked;

            let errors = [];
            
            // 1. Kiểm tra Họ và Tên
            if (name === "") {
                errors.push("Vui lòng nhập Họ và Tên.");
            }

            // 2. Kiểm tra Số điện thoại
            const phoneRegex = /^(0\d{9})$/; // Regex: Bắt đầu bằng 0, theo sau là 9 số (tổng 10 số)
            if (phone === "") {
                errors.push("Vui lòng nhập Số điện thoại.");
            } else if (!phoneRegex.test(phone)) {
                errors.push("Số điện thoại không hợp lệ (phải là 10 số, bắt đầu bằng 0).");
            }

            // 3. Kiểm tra Địa chỉ (chỉ khi không nhận tại cửa hàng)
            if (!isPickup) {
                if (address === "") {
                    errors.push("Vui lòng nhập Địa chỉ.");
                }
            }

            // 4. Kiểm tra nếu có lỗi
            if (errors.length > 0) {
                // Ngăn form gửi đi
                event.preventDefault(); 
                
                // Hiển thị tất cả lỗi cho người dùng
                alert(errors.join("\n"));
            }
            // Nếu không có lỗi, form sẽ tự động submit
        });
    }
});
  
function pay() {window.location.href = "/payment";}

function formatCurrency(number) {
    return new Intl.NumberFormat('vi-VN').format(Math.round(number));
}

function updateCartTotalFromServer() {
  const cartTotal = parseFloat(document.getElementById('cartTotalPriceInput').value) || 0;
  const tamtinhEl = document.getElementById('tamtinh-value');
  const totalEl = document.getElementById('total-value');
  // MỚI: Lấy phần tử hiển thị phí ship
  const shippingFeeDisplay = document.getElementById("shipping-fee-value");
  
  const shippingFeeInput = document.getElementById('shippingFeeInput'); // Lấy thẻ input ẩn
  
  const isPickupAtStore = document.getElementById("ckbstore").checked;
  const shippingFee = isPickupAtStore ? 0 : 30000;

  // CẬP NHẬT: Thay đổi text của phí vận chuyển
  if (shippingFeeDisplay) {
      shippingFeeDisplay.textContent = formatCurrency(shippingFee);
  }

  // CẬP NHẬT GIÁ TRỊ CHO INPUT ẨN
  if (shippingFeeInput) {
      shippingFeeInput.value = shippingFee;
  }

  if (tamtinhEl) tamtinhEl.textContent = formatCurrency(cartTotal);
  const finalTotal = cartTotal + shippingFee;
  if (totalEl) totalEl.textContent = formatCurrency(finalTotal);

  document.getElementById('finalTotalInput').value = finalTotal;
}

function applyDiscount() {
  const discountselect = document.getElementById('voucher');
  const selectedOption = discountselect.options[discountselect.selectedIndex];
  const cartTotal = parseFloat(document.getElementById('cartTotalPriceInput').value) || 0;
  // MỚI: Lấy phần tử hiển thị phí ship
  const shippingFeeDisplay = document.getElementById("shipping-fee-value");
  const shippingFeeInput = document.getElementById('shippingFeeInput'); // Lấy thẻ input ẩn

  const isPickupAtStore = document.getElementById("ckbstore").checked;
  const shippingFee = isPickupAtStore ? 0 : 30000;

  // CẬP NHẬT: Thay đổi text của phí vận chuyển
  if (shippingFeeDisplay) {
      shippingFeeDisplay.textContent = formatCurrency(shippingFee);
  }

  // CẬP NHẬT GIÁ TRỊ CHO INPUT ẨN
    if (shippingFeeInput) {
        shippingFeeInput.value = shippingFee;
    }

  let discountAmount = 0;
  let discountId = ""; 
  if (selectedOption && selectedOption.value) {
          const fixedDiscountAmount = parseFloat(selectedOption.getAttribute('data-value')) || 0;
          const conditionValue = parseFloat(selectedOption.getAttribute('data-condition-value')) || 0;

          if (cartTotal >= conditionValue) {
              if (!isNaN(fixedDiscountAmount)) {
                  discountAmount = fixedDiscountAmount;
                  discountId = selectedOption.value;
              }
          } else {
              const requiredAmountFormatted = formatCurrency(conditionValue);
              alert(`Mã này chỉ áp dụng cho đơn hàng từ ${requiredAmountFormatted}đ trở lên.`);
              discountselect.value = ""; 
          }
  }

    if (discountAmount > cartTotal) {
        discountAmount = cartTotal;
    }
    const finalTotalEl = cartTotal - discountAmount + shippingFee;

    document.getElementById('giamgia-value').innerText = formatCurrency(discountAmount);
    document.getElementById('total-value').innerText = formatCurrency(finalTotalEl);

    document.getElementById('discountIdInput').value = discountId;
    document.getElementById('finalTotalInput').value = finalTotalEl;
}