
let detailModal = null;
let editModal = null;

function getTrangThaiDisplay(code) {
    switch(code) {
        case 'ChoXuLy': return 'Chờ xử lý';
        case 'XacNhan': return 'Xác nhận';
        case 'DangGiao': return 'Đang giao';
        case 'HoanThanh': return 'Hoàn tất';
        case 'DaHuy': return 'Hủy bỏ';
        default: return code;
    }
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
            setText('detail-trang-thai', getTrangThaiDisplay(order.trangThai));
            setText('detail-ngay-tao', order.ngayTao);
            setText('detail-khach-hang', order.khachHang.tenKhachHang || '-');
            setText('detail-SDT-khach-hang', order.khachHang.soDienThoai || '-');
            setText('detail-email-khach-hang', order.khachHang.email || '-');
            setText('detail-tong-tien', order.tongTien.toLocaleString('vi-VN') + ' VNĐ');    
            setText('detail-ghi-chu', order.ghiChu);
            setText('detail-dia-chi', order.diaChiGiaoHang || '-');
            setText('detail-khuyen-mai',order.khuyenMaiId);
            setText('detail-phuong-thuc', order.phuongThucThanhToanId);
     
            // Danh sách sản phẩm
            const itemsContainer = document.getElementById('detail-items');
            if (itemsContainer) {
                itemsContainer.innerHTML = '';
                const details = Array.isArray(order.orderDetail) ? order.orderDetail : [];
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
                        left.textContent = `${d.sanPham.tenSanPham}`;
                        const right = document.createElement('span');
                        right.className = 'badge bg-secondary rounded-pill';
                        right.textContent = `Khối lượng:${d.soLuong}kg - giá: ${d.gia.toLocaleString('vi-VN')} VNĐ`;
                        row.appendChild(left);
                        row.appendChild(right);
                        itemsContainer.appendChild(row);
                    });
                }
            }
        // Hiển thị modal
        detailModal.show();
        })
        .catch(err => {
            console.error(err);
            alert('Lỗi tải chi tiết đơn hàng');
        });
}

// chức năng sửa đơn hàng
// Hàm hiển thị modal sửa đơn hàng
function showEditOrder(ma_don_hang) {
    // document.getElementById('edit-ma-don-hang-display').value = ma_don_hang;
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
            //thong tin khach hang
            document.getElementById('edit-khach-hang-id').value = order.khachHangId || '';
            document.getElementById('edit-ten-nguoi-mua').value = order.khachHang.tenKhachHang || '';
            document.getElementById('edit-email-nguoi-mua').value = order.khachHang.email;
            document.getElementById('edit-sdt-nguoi-mua').value = order.khachHang.soDienThoai;
            //thong tin don hang
            document.getElementById('edit-tong-tien').value = order.tongTien.toLocaleString('vi-VN') + ' VNĐ';
            document.getElementById('edit-phuong-thuc').value = order.phuongThucThanhToanId;
            document.getElementById('edit-ten-khuyen-mai').value = order.khuyenMaiId;
            document.getElementById('edit-dia-chi').value = order.diaChiGiaoHang;
            document.getElementById('edit-ghi-chu').value = order.ghiChu;
            document.getElementById('edit-ma-don-hang').value = order.maDonHang || '';
            document.getElementById('edit-ma-don-hang-display').value = order.maDonHang;
            document.getElementById('edit-trang-thai').value = order.trangThai;
            document.getElementById('edit-ngay-tao').value = order.ngayTao;

            // THÊM: Hiển thị danh sách sản phẩm
            const editItems = document.getElementById('edit-items');
            editItems.innerHTML = '';
            
            if (order.orderDetail && order.orderDetail.length > 0) {
                order.orderDetail.forEach(detail => {
                    const itemDiv = document.createElement('div');
                    itemDiv.className = 'list-group-item';
                    // Lưu thông tin sản phẩm vào data attributes
                    itemDiv.setAttribute('data-sanpham-id', detail.sanPham.id);
                    itemDiv.setAttribute('data-so-luong', detail.soLuong);
                    itemDiv.setAttribute('data-gia', detail.gia);
                    
                    itemDiv.innerHTML = `
                        <div class="d-flex justify-content-between align-items-center">
                            <span><strong>${detail.sanPham.tenSanPham}</strong></span>
                            <span>Khối lượng: <strong>${detail.soLuong}</strong>kg - Giá: <strong>${detail.gia.toLocaleString('vi-VN')}</strong> VNĐ</span>
                        </div>
                    `;
                    editItems.appendChild(itemDiv);
                });
            } else {
                editItems.innerHTML = '<div class="list-group-item">Không có sản phẩm nào</div>';
            }

            // Hiển thị modal
            editModal.show();

        })
        .catch(error => {
            console.error('Error:', error);
            alert('Lỗi: ' + error.message);
        });

    // Hiển thị modal
    
}

// Hàm xử lý cập nhật đơn hàng
function handleUpdateOrder() {
        const maDonHang = document.getElementById('edit-ma-don-hang').value;   
        // Gửi object giống cấu trúc orderEntity
        const updateData = {
            trangThai: document.getElementById('edit-trang-thai').value,
            diaChiGiaoHang: document.getElementById('edit-dia-chi').value,
            ghiChu: document.getElementById('edit-ghi-chu').value,
            phuongThucThanhToanId: parseInt(document.getElementById('edit-phuong-thuc').value) || null,
            khuyenMaiId: document.getElementById('edit-ten-khuyen-mai').value ? 
                        parseInt(document.getElementById('edit-ten-khuyen-mai').value) : null,
            tongTien: document.getElementById('edit-tong-tien').value.replace(/[^\d]/g, '') || 0
        };
        
        fetch(`/api/admin/order/update/${maDonHang}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updateData)
        })
        .then(response => {
            if (response.ok) {
                alert('Cập nhật đơn hàng thành công!');          
                editModal.hide();
                location.reload();
            } else {
                return response.text().then(text => {
                    throw new Error(text || 'Có lỗi xảy ra');
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Lỗi: ' + error.message);
        });
}

function initEditOrderFormSubmit(){
    const editForm = document.getElementById('editOrderForm');
    if(editForm){
        editForm.addEventListener('submit', function(e) {
            e.preventDefault();
            handleUpdateOrder();
        });
    }    
}

document.addEventListener('DOMContentLoaded', function(){   
    detailModal = new bootstrap.Modal(document.getElementById('orderDetailModal'));
    editModal = new bootstrap.Modal(document.getElementById('editOrderModal'));

    initEditOrderFormSubmit();
});














