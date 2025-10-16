
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
            // Thông tin tổng quan - xử lý từ entity
            const setText = (id, value) => {    
                const el = document.getElementById(id);
                if (el) el.textContent = value ?? '-';
            };
            setText('detail-ma-don-hang', order.maDonHang);
            setText('detail-trang-thai', getTrangThaiDisplay(order.trangThai));
            setText('detail-ngay-tao', new Date(order.ngayTao).toLocaleDateString('vi-VN'));
            setText('detail-khach-hang', order.khachHang?.tenKhachHang || '-');
            setText('detail-email-khach-hang', order.khachHang?.email || '-');
            setText('detail-delivery', order.phiShip ? order.phiShip.toLocaleString('vi-VN') + ' VNĐ' : '-');
            setText('detail-SDT-khach-hang', order.khachHang?.soDienThoai || '-');
            setText('detail-ten-nguoi-nhan', order.tenNguoiNhan || '-');
            setText('detail-SDT-nguoi-nhan', order.soDienThoaiNguoiNhan || '-');
            setText('detail-tong-tien', order.tongTien ? order.tongTien.toLocaleString('vi-VN') + ' VNĐ' : '-');    
            setText('detail-ghi-chu', order.ghiChu || '-');
            setText('detail-dia-chi', order.diaChiGiaoHang || '-');
            setText('detail-khuyen-mai', order.discount?.tenKM || '-');
            setText('detail-phuong-thuc', order.phuongThucThanhToan?.name || '-');
     
            // Danh sách sản phẩm
            const itemsContainer = document.getElementById('detail-items');
            itemsContainer.innerHTML = '';
            // order.items đổi thành order.orderDetail
            if (order.orderDetail && order.orderDetail.length > 0) { // << ĐÃ SỬA
                order.orderDetail.forEach(d => { // << ĐÃ SỬA
                    const row = document.createElement('div');
                    row.className = 'list-group-item d-flex justify-content-between align-items-center';
                    // d.tenSanPham đổi thành d.sanPham.tenSanPham
                    row.innerHTML = `<span>${d.sanPham?.tenSanPham || 'N/A'}</span>
                                     <span class="badge bg-secondary rounded-pill">Số lượng: ${d.soLuong} - Giá: ${d.gia.toLocaleString('vi-VN')} VNĐ</span>`;
                    itemsContainer.appendChild(row);
                });
            } else {
                itemsContainer.innerHTML = '<div class="list-group-item">Không có sản phẩm</div>';
            }
            detailModal.show();
        })
        .catch(err => console.error('Lỗi tải chi tiết đơn hàng:', err));
}

// chức năng sửa đơn hàng
// Hàm hiển thị modal sửa đơn hàng
function showEditOrder(ma_don_hang) {
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
            //thong tin khach hang - xử lý từ entity
            document.getElementById('edit-khach-hang-id').value = order.khachHang?.id || '';
            document.getElementById('edit-ten-nguoi-mua').value = order.khachHang?.tenKhachHang || '';
            document.getElementById('edit-email-nguoi-mua').value = order.khachHang?.email || '';
            document.getElementById('edit-sdt-nguoi-mua').value = order.khachHang?.soDienThoai || '';
            document.getElementById('edit-ten-nguoi-nhan').value = order.tenNguoiNhan || '';
            document.getElementById('edit-sdt-nguoi-nhan').value = order.soDienThoaiNguoiNhan || '';
            
            //thong tin don hang
            document.getElementById('edit-phi-ship').value = order.phiShip ? order.phiShip.toLocaleString('vi-VN') + ' VNĐ' : '';
            document.getElementById('edit-tong-tien').value = order.tongTien ? order.tongTien.toLocaleString('vi-VN') + ' VNĐ' : '';
            document.getElementById('edit-phuong-thuc').value = order.phuongThucThanhToan?.name || '';
            document.getElementById('edit-phuong-thuc-id').value = order.phuongThucThanhToan?.id || '';
            document.getElementById('edit-ten-khuyen-mai').value = order.discount?.tenKM || '';
            document.getElementById('edit-ten-khuyen-mai-id').value = order.discount?.id || '';
            document.getElementById('edit-dia-chi').value = order.diaChiGiaoHang || '';
            document.getElementById('edit-ghi-chu').value = order.ghiChu || '';
            document.getElementById('edit-ma-don-hang').value = order.maDonHang || '';
            document.getElementById('edit-ma-don-hang-display').value = order.maDonHang || '';
            document.getElementById('edit-trang-thai').value = order.trangThai || '';
            document.getElementById('edit-ngay-tao').value = new Date(order.ngayTao).toLocaleDateString('vi-VN');

            // THÊM: Hiển thị danh sách sản phẩm
            const editItems = document.getElementById('edit-items');
            editItems.innerHTML = '';
            
            if (order.orderDetail && order.orderDetail.length > 0) {
                order.orderDetail.forEach(detail => {
                    const itemDiv = document.createElement('div');
                    itemDiv.className = 'list-group-item';
                    // Lưu thông tin sản phẩm vào data attributes
                    itemDiv.setAttribute('data-sanpham-id', detail.sanPham?.id || '');
                    itemDiv.setAttribute('data-so-luong', detail.soLuong || '');
                    itemDiv.setAttribute('data-gia', detail.gia || '');
                    
                    itemDiv.innerHTML = `
                        <div class="d-flex justify-content-between align-items-center">
                            <span><strong>${detail.sanPham?.tenSanPham || 'N/A'}</strong></span>
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
}

// Hàm xử lý cập nhật đơn hàng
function handleUpdateOrder() {
        const maDonHang = document.getElementById('edit-ma-don-hang').value;   
        
        // Chỉ gửi các trường admin được phép sửa
        const updateData = {
            trangThai: document.getElementById('edit-trang-thai').value,
            diaChiGiaoHang: document.getElementById('edit-dia-chi').value,
            tenNguoiNhan: document.getElementById('edit-ten-nguoi-nhan').value,
            soDienThoaiNguoiNhan: document.getElementById('edit-sdt-nguoi-nhan').value,
            ghiChu: document.getElementById('edit-ghi-chu').value,
            phuongThucThanhToan: { // Gửi object con chứa id
                id: parseInt(document.getElementById('edit-phuong-thuc-id').value) || null
            },
            discount: { // Gửi object con chứa id
                id: parseInt(document.getElementById('edit-ten-khuyen-mai-id').value) || null
            }
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














