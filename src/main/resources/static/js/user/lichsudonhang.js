// Biến toàn cục để lưu trữ đối tượng modal của Bootstrap
let orderModal = null;
let updateButton = null;

/**
 * Hàm được gọi trực tiếp từ thuộc tính onclick trong file HTML
 * để lấy dữ liệu và hiển thị modal chi tiết đơn hàng.
 * @param {string} orderId - ID của đơn hàng cần xem chi tiết.
 */
function showLichSuDonHang(orderId) {
    if (!orderId) {
        console.error("Order ID không hợp lệ.");
        return;
    }

    // Gọi API để lấy chi tiết đơn hàng
    fetch(`/api/orders/${orderId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Không thể tải chi tiết đơn hàng");
            }
            return response.json();
        })
        .then(data => {
            // 1. Gán dữ liệu vào các trường trong modal
            document.getElementById('modalOrderId').value = data.id;
            document.getElementById('modal-order-maDonHang').textContent = data.maDonHang;
            document.getElementById('modal-order-date').textContent = formatDate(data.ngayTao);
            document.getElementById('modal-order-total').textContent = formatCurrency(data.tongTien);

            // 2. Cập nhật trạng thái và màu sắc
            const statusElement = document.getElementById('modal-order-status');
            statusElement.textContent = data.trangThaiDisplay || data.trangThai;
            statusElement.className = 'order-status'; // Reset class
            if (data.trangThaiDisplay === 'Chờ xử lý') statusElement.classList.add('status-processing');
            else if (data.trangThaiDisplay === 'Hoàn tất') statusElement.classList.add('status-completed');
            else if (data.trangThaiDisplay === 'Đã hủy') statusElement.classList.add('status-cancelled');
            else if (data.trangThaiDisplay === 'Đang giao') statusElement.classList.add('status-shipping');
            else if (data.trangThaiDisplay === 'Xác nhận') statusElement.classList.add('status-confirmed');

            // 3. Hiển thị danh sách sản phẩm
            const productList = document.getElementById('modal-product-list');
            productList.innerHTML = ''; // Xóa danh sách cũ
            if (data.orderDetail && data.orderDetail.length > 0) {
                data.orderDetail.forEach(detail => {
                    const li = document.createElement('li');
                    li.className = 'list-group-item';
                    li.textContent = `${detail.sanPham.tenSanPham} - SL: ${detail.soLuong}`;
                    productList.appendChild(li);
                });
            } else {
                productList.innerHTML = '<li class="list-group-item">Không có sản phẩm nào</li>';
            }

            // 4. Điền thông tin người nhận vào form
            document.getElementById('modal-recipient-name').value = data.tenNguoiNhan || '';
            document.getElementById('modal-recipient-phone').value = data.soDienThoaiNhan || '';
            document.getElementById('modal-recipient-address').value = data.diaChiGiaoHang || '';

            // 5. Hiển thị modal
            if (orderModal) {
                orderModal.show();
            }
        })
        .catch(error => {
            console.error("Lỗi khi lấy chi tiết đơn hàng:", error);
            alert("Đã xảy ra lỗi, không thể hiển thị chi tiết đơn hàng.");
        });
}

/**
 * Xử lý sự kiện click cho nút "Lưu thay đổi" trong modal.
 */
function handleUpdateOrder() {
    const orderId = document.getElementById('modalOrderId').value;
    
    const updateData = {
        tenNguoiNhan: document.getElementById('modal-recipient-name').value.trim(),
        soDienThoaiNhan: document.getElementById('modal-recipient-phone').value.trim(),
        diaChiGiaoHang: document.getElementById('modal-recipient-address').value.trim()
    };

    // --- Validation cơ bản ---
    if (!updateData.tenNguoiNhan || !updateData.soDienThoaiNhan || !updateData.diaChiGiaoHang) {
        alert("Vui lòng điền đầy đủ thông tin người nhận!");
        return;
    }

    const phoneRegex = /^(0|\+84)[0-9]{9,10}$/;
    if (!phoneRegex.test(updateData.soDienThoaiNhan)) {
        alert("Số điện thoại không hợp lệ!");
        return;
    }
    // --- Kết thúc Validation ---

    // Gọi API để cập nhật
    fetch(`/api/orders/${orderId}/update-recipient`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updateData)
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(text || "Không thể cập nhật đơn hàng");
            });
        }
        return response.json();
    })
    .then(data => {
        alert("Cập nhật thông tin đơn hàng thành công!");
        if (orderModal) {
            orderModal.hide();
        }
        location.reload(); // Tải lại trang để cập nhật danh sách
    })
    .catch(error => {
        console.error("Lỗi khi cập nhật đơn hàng:", error);
        alert("Lỗi: " + error.message);
    });
}


// --- Chạy khi trang đã tải xong ---
document.addEventListener('DOMContentLoaded', function () {
    const modalElement = document.getElementById('orderDetailModal');
    updateButton = document.getElementById('saveChangesBtn');

    // 1. Khởi tạo đối tượng Modal của Bootstrap
    if (modalElement) {
        orderModal = new bootstrap.Modal(modalElement);
    }

    // 2. Gán sự kiện cho nút "Lưu thay đổi"
    if (updateButton) {
        updateButton.addEventListener('click', handleUpdateOrder);
    }
});


// --- Các hàm phụ trợ ---
function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}

function formatCurrency(amount) {
    if (amount == null) return '0đ';
    return amount.toLocaleString('vi-VN') + 'đ';
}