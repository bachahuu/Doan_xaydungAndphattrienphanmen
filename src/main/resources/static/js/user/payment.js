document.addEventListener('DOMContentLoaded', function () {

    // Ghi chú đơn hàng
    const note = document.getElementById("orderNote");
    const count = document.getElementById("charCount");
        note.addEventListener("input", function () {
            count.textContent = `${note.value.length} / 500 ký tự`;
        });


    const checkoutForm = document.getElementById('checkoutForm');
    const completePayButton = document.getElementById('complete_pay');
    

    
    // Xử lý nút thanh toán
    if (completePayButton) {
        completePayButton.addEventListener('click', function (event) {
            event.preventDefault(); 
            handleFormSubmit();
        });
    }

    // Bước 2: Xử lý các thành phần khác như modal sau
    // let bootstrapModal = null;

    // if (successModalElement) {
    //     // Khởi tạo modal để sẵn sàng sử dụng
    //     bootstrapModal = new bootstrap.Modal(successModalElement, {
    //         backdrop: 'static',
    //         keyboard: false
    //     });
    
    //     // Xử lý nút "Trang Chủ" trong modal
    //     const backToHomeBtn = document.getElementById('backToCartBtn');
    //     if (backToHomeBtn) {
    //         backToHomeBtn.addEventListener('click', function (e) {
    //             e.preventDefault();
    //             window.location.href = '/home-static'; // Chuyển về trang chủ
    //         });
    //     }
    // }    



    function handleFormSubmit() {
        const selectedPaymentMethod = document.querySelector('input[name="paymentMethod"]:checked');
        if (!selectedPaymentMethod) {
            alert('Vui lòng chọn một phương thức thanh toán!');
            return;
        }

        const paymentType = selectedPaymentMethod.getAttribute('data-payment-type');
        const formData = new FormData(checkoutForm);
        formData.set('paymentMethod', selectedPaymentMethod.value);

        // Lấy giá trị từ textarea ghi chú và thêm vào formData
        const orderNoteValue = document.getElementById('orderNote').value;
        formData.append('orderNote', orderNoteValue);

        // Vô hiệu hóa nút bấm
        completePayButton.disabled = true;
        completePayButton.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Đang xử lý...';

        if(paymentType === 'momo'){
            // gọi api thanh toan momo
            fetch("/payment/momo/create",{
                method: 'POST',
                body: new URLSearchParams(formData)
            })
            .then(response => response.json().then(data => ({ ok: response.ok, data })))
            .then(({ ok, data }) => {
                if (!ok) {
                    throw new Error(data.message || 'Có lỗi xảy ra trong quá trình tạo đơn hàng MoMo.');
                }
            // Chuyển hướng đến trang thanh toán của MoMo
            window.location.href = data.payUrl;
            })
            .catch(error => {
            console.error('Error:', error);
            alert('Lỗi: ' + error.message);
            // Kích hoạt lại nút
            completePayButton.disabled = false;
            completePayButton.textContent = 'Xác nhận thanh toán';
            })
        }else{
            // Xử lý thanh toán COD
            fetch("/orders/checkout", {
            method: 'POST',
            body: new URLSearchParams(formData)
            })
            .then(response => {
                // Chuyển response thành JSON để đọc nội dung message
                return response.json().then(data => {
                    // Nếu response không phải là 2xx (ví dụ 400, 500), ném ra lỗi
                    if (!response.ok) {
                        throw new Error(data.message || 'Có lỗi xảy ra, vui lòng thử lại.');
                    }
                    // Nếu thành công, trả về dữ liệu (dù có thể không dùng)
                    return data;
                });
            })
            .then(data => {
                // Hiển thị thông báo thành công đơn giản
                console.log('Success:', data.message);
                const orderCode = data.maDonHang ? ` Mã đơn hàng của bạn là: ${data.maDonHang}.` : '';
                alert(`Đặt hàng thành công!${orderCode} Chúng tôi sẽ xác nhận và gửi hàng đến bạn sớm nhất.`);
                // Chuyển về trang chủ sau khi đóng alert
                window.location.href = '/home-static';
            })
            .catch(error => {
                // Xử lý khi có lỗi mạng hoặc lỗi từ server
                console.error('Error:', error);
                alert('Lỗi: ' + error.message);
            })
            .finally(() => {
                // Luôn luôn kích hoạt lại nút bấm sau khi hoàn tất
                completePayButton.disabled = false;
                completePayButton.textContent = 'Xác nhận thanh toán';
            });
        }
  
    }


    
});