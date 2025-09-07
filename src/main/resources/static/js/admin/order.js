// order-management.js - JavaScript cho trang quản lý đơn hàng

// Hàm cập nhật trạng thái đơn hàng
function update_trang_thai(ma_don_hang, newStatus) {
    document.getElementById('status-' + ma_don_hang).innerText = newStatus;
    if (newStatus === 'huy_bo') {
        var actionGroup = document.getElementById('action-group-' + ma_don_hang);
        if (actionGroup) {
            actionGroup.style.display = 'none';
        }
    }
}

// Hàm lấy thông tin chi tiết đơn hàng
function fetchOrderDetails(maDonHang) {
    //  Sử dụng API call thực tế
    /*
    fetch(`/admin/orders/details/${maDonHang}`)
        .then(response => response.json())
        .then(data => {
            populateOrderDetailModal(data);
        })
        .catch(error => {
            console.error('Error fetching order details:', error);
            alert('Không thể tải thông tin đơn hàng. Vui lòng thử lại!');
        });
    */
    
    const data = sampleData[maDonHang] || {};
    populateOrderDetailModal(data);
    
    // Lưu mã đơn hàng để sử dụng khi sửa
    document.getElementById('orderDetailModal').setAttribute('data-ma-don-hang', maDonHang);
}

// Hàm điền dữ liệu vào modal chi tiết
function populateOrderDetailModal(data) {
    document.getElementById('modal-ten-nguoi-mua').textContent = data.tenNguoiMua || '-';
    document.getElementById('modal-email-nguoi-mua').textContent = data.emailNguoiMua || '-';
    document.getElementById('modal-sdt-nguoi-mua').textContent = data.sdtNguoiMua || '-';
    document.getElementById('modal-ten-khuyen-mai').textContent = data.tenKhuyenMai || '-';
    document.getElementById('modal-tong-tien').textContent = data.tongTien || '-';
    document.getElementById('modal-phuong-thuc').textContent = data.phuongThucThanhToan || '-';
    document.getElementById('modal-dia-chi').textContent = data.diaChiNguoiMua || '-';
}

// Hàm điền dữ liệu vào form sửa
function populateEditForm() {
    const maDonHang = document.getElementById('orderDetailModal').getAttribute('data-ma-don-hang');
    
    document.getElementById('edit-ma-don-hang').value = maDonHang;
    document.getElementById('edit-ten-nguoi-mua').value = document.getElementById('modal-ten-nguoi-mua').textContent;
    document.getElementById('edit-email-nguoi-mua').value = document.getElementById('modal-email-nguoi-mua').textContent;
    document.getElementById('edit-sdt-nguoi-mua').value = document.getElementById('modal-sdt-nguoi-mua').textContent;
    
    // Xử lý trường chiết khấu (có thể là '-' hoặc 'Không có')
    const khuyenMaiText = document.getElementById('modal-ten-khuyen-mai').textContent;
    document.getElementById('edit-ten-khuyen-mai').value = (khuyenMaiText === '-' || khuyenMaiText === 'Không có') ? '' : khuyenMaiText;
    
    document.getElementById('edit-tong-tien').value = document.getElementById('modal-tong-tien').textContent;
    document.getElementById('edit-phuong-thuc').value = document.getElementById('modal-phuong-thuc').textContent;
    document.getElementById('edit-dia-chi').value = document.getElementById('modal-dia-chi').textContent;
}

// Hàm khởi tạo trang khi load
function initializeOrderManagement() {
    // Ẩn các action group cho đơn hàng đã hủy
    if (typeof window.trangThaiDonHang !== 'undefined') {
        Object.keys(window.trangThaiDonHang).forEach(function(ma_don_hang) {
            if (window.trangThaiDonHang[ma_don_hang] === 'huy_bo') {
                var actionGroup = document.getElementById('action-group-' + ma_don_hang);
                if (actionGroup) {
                    actionGroup.style.display = 'none';
                }
            }
        });
    }
}

// Event listeners
document.addEventListener('DOMContentLoaded', function() {
    
    // Khởi tạo trang
    initializeOrderManagement();
    
    // Xử lý khi nhấn nút "Chi tiết đơn hàng"
    document.addEventListener('click', function(e) {
        if (e.target.closest('.btn-order-detail')) {
            const button = e.target.closest('.btn-order-detail');
            const maDonHang = button.getAttribute('data-ma-don-hang');
            
            // Hiển thị loading hoặc thông báo
            console.log('Loading order details for:', maDonHang);
            
            // Gọi hàm để lấy thông tin chi tiết đơn hàng
            fetchOrderDetails(maDonHang);
        }
    });
    
    // Xử lý khi nhấn nút sửa trong modal chi tiết
    document.addEventListener('click', function(e) {
        if (e.target.closest('.btn-edit-order')) {
            // Lấy dữ liệu từ modal chi tiết và điền vào form sửa
            populateEditForm();
        }
    });
    
    // Xử lý submit form cập nhật (optional)
    const editForm = document.querySelector('#editOrderModal form');
    if (editForm) {
        editForm.addEventListener('submit', function(e) {
            // Có thể thêm validation hoặc xử lý đặc biệt ở đây
            console.log('Submitting order update form');
            
            // Ví dụ validation đơn giản
            const requiredFields = editForm.querySelectorAll('input[required], textarea[required]');
            let isValid = true;
            
            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    field.classList.add('is-invalid');
                    isValid = false;
                } else {
                    field.classList.remove('is-invalid');
                }
            });
            
            if (!isValid) {
                e.preventDefault();
                alert('Vui lòng điền đầy đủ thông tin bắt buộc!');
            }
        });
    }
});

// Legacy support cho window.onload (giữ lại để tương thích)
window.addEventListener('load', function() {
    initializeOrderManagement();
});