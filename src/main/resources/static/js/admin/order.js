// order-management.js - JavaScript cho trang quản lý đơn hàng
document.addEventListener('DOMContentLoaded', function(){   
    // Xử lý form sửa đơn hàng
    const editForm = document.getElementById('editOrderForm');
    if (editForm) {
        editForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const maDonHang = document.getElementById('edit-ma-don-hang').value;
            const trangThai = document.getElementById('edit-trang-thai').value;
            
            // Cập nhật trạng thái
            update_trang_thai(maDonHang, trangThai);
            
            // Đóng modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('editOrderModal'));
            modal.hide();
        });
    }
});

// Hàm hiển thị modal sửa đơn hàng
function showEditOrder(ma_don_hang) {
    document.getElementById('edit-ma-don-hang-display').value = ma_don_hang;
    
    // Lấy chi tiết đơn hàng để điền vào form
    fetch(`/api/admin/order/detail/${ma_don_hang}`)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Không thể lấy thông tin đơn hàng');
            }
        })
        .then(order => {
            // Điền thông tin vào form
            document.getElementById('edit-ten-nguoi-mua').value = order.khachHangId || '';
            document.getElementById('edit-tong-tien').value = order.tongTien || '';
            document.getElementById('edit-trang-thai').value = order.trangThai || 'Cho_xu_ly';
            document.getElementById('edit-ngay-tao').value = order.ngayTao;
            document.getElementById('edit-ten-nguoi-mua').value = order.khachHangId;
            document.getElementById('edit-tong-tien').value = order.tongTien;
            document.getElementById('edit-phuong-thuc').value = order.phuongThucThanhToanId;
            document.getElementById('edit-ten-khuyen-mai').value = order.khuyenMaiId;
            document.getElementById('edit-dia-chi').value = order.diaChiGiaoHang;
            document.getElementById('edit-ghi-chu').value = order.ghiChu;
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Lỗi: ' + error.message);
        });
}

// Hàm hiển thị chi tiết đơn hàng
function showOrderDetail(ma_don_hang) {
    // Lấy chi tiết đơn hàng và render vào modal dạng form + list sản phẩm
    fetch(`/api/admin/order/detail/${ma_don_hang}`)
        .then(response => {
            if (!response.ok) throw new Error('Không thể tải chi tiết đơn hàng');
            return response.json();
        })
        .then(order => {
            // Thông tin tổng quan
            const setText = (id, value) => {    
                const el = document.getElementById(id);
                if (el) el.textContent = value ?? '-';
            };
            setText('detail-ma-don-hang', order.maDonHang);
            setText('detail-trang-thai', order.trangThai);
            setText('detail-ngay-tao', order.ngayTao);
            setText('detail-khach-hang', order.khachHangId);
            setText('detail-SDT-khach-hang', order.soDienThoaiNguoiMua || order.sdtNguoiMua || order.sdt || '-');
            setText('detail-tong-tien', order.tongTien);    
            setText('detail-ghi-chu', order.ghiChu);
            setText('detail-dia-chi', order.diaChiGiaoHang || '-');
            setText('detail-khuyen-mai',order.khuyenMaiId);
            // Danh sách sản phẩm
            const itemsContainer = document.getElementById('detail-items');
            if (itemsContainer) {
                itemsContainer.innerHTML = '';
                const details = Array.isArray(order.chiTietDonHang) ? order.chiTietDonHang : [];
                if (details.length === 0) {
                    const empty = document.createElement('div');
                    empty.className = 'list-group-item';
                    empty.textContent = 'Không có sản phẩm';
                    itemsContainer.appendChild(empty);
                } else {
                    details.forEach(d => {
                        const row = document.createElement('div');
                        row.className = 'list-group-item d-flex justify-content-between align-items-center';
                        const left = document.createElement('div');
                        // Ở đây chưa có tên sản phẩm, chỉ có sanPhamId. Hiển thị tạm sanPhamId.
                        left.textContent = `Sản phẩm #${d.sanPhamId}`;
                        const right = document.createElement('span');
                        right.className = 'badge bg-secondary rounded-pill';
                        right.textContent = `x${d.soLuong}`;
                        row.appendChild(left);
                        row.appendChild(right);
                        itemsContainer.appendChild(row);
                    });
                }
            }
        })
        .catch(err => {
            console.error(err);
            alert('Lỗi tải chi tiết đơn hàng');
        });
}
// Hàm cập nhật trạng thái đơn hàng
function update_trang_thai(ma_don_hang, newStatus) {
    fetch(`/api/admin/order/update-status/${ma_don_hang}?trangThai=${newStatus}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            // Cập nhật UI
            document.getElementById('status-' + ma_don_hang).innerText = newStatus;
            
            // Hiển thị thông báo thành công
            console.log('Cập nhật trạng thái thành công');
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Lỗi cập nhật trạng thái');
            });
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Lỗi: ' + error.message);
    });
}


