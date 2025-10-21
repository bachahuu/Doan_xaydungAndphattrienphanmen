document.addEventListener('DOMContentLoaded', function() {
    // **TỰ ĐỘNG REFRESH TRẠNG THÁI MỖI 30 GIÂY**
    setInterval(function() {
        location.reload();
    }, 30000);

    // Xử lý mở modal sửa khuyến mãi
    document.querySelectorAll('.btn-update').forEach(btn => {
        btn.addEventListener('click', function() {
            const id = this.getAttribute('data-id');
            
            fetch(`/admin/discount/api/get/${id}`)
                .then(res => {
                    if (!res.ok) throw new Error('Không tìm thấy khuyến mãi!');
                    return res.json();
                })
                .then(data => {
                    // **TỰ ĐỘNG HIỂN THỊ TRẠNG THÁI HIỆN TẠI**
                    document.getElementById('update_id').value = data.id || '';
                    document.getElementById('update_maKM').value = data.maKM || '';
                    document.getElementById('update_tenKM').value = data.tenKM || '';
                    document.getElementById('update_giaTri').value = data.giaTri || 0;
                    document.getElementById('update_giaTriDonHangToiThieu').value = data.giaTriDonHangToiThieu || 0;
                    document.getElementById('update_ngayBatDau').value = data.ngayBatDau ? data.ngayBatDau.split('T')[0] : '';
                    document.getElementById('update_ngayKetThuc').value = data.ngayKetThuc ? data.ngayKetThuc.split('T')[0] : '';
                    document.getElementById('update_trangThai').value = data.trangThai || 2;
                    
                    document.getElementById('updateDiscountError').classList.add('d-none');
                })
                .catch(error => {
                    console.error('Error fetching discount:', error);
                    alert(error.message || 'Đã xảy ra lỗi khi lấy dữ liệu!');
                });
        });
    });
    // Xử lý mở modal sửa khuyến mãi
    document.querySelectorAll('.btn-update').forEach(btn => {
        btn.addEventListener('click', function() {
            const id = this.getAttribute('data-id');
            console.log('Edit button clicked, ID:', id);
            
            if (!id || id === 'undefined' || id === 'null') {
                alert('ID khuyến mãi không hợp lệ!');
                return;
            }
            
            fetch(`/admin/discount/api/get/${id}`)
                .then(res => {
                    if (!res.ok) {
                        throw new Error('Không tìm thấy khuyến mãi!');
                    }
                    return res.json();
                })
                .then(data => {
                    console.log('Discount data:', data);
                    document.getElementById('update_id').value = data.id || '';
                    document.getElementById('update_maKM').value = data.maKM || '';
                    document.getElementById('update_tenKM').value = data.tenKM || '';
                    document.getElementById('update_giaTri').value = data.giaTri || 0;
                    document.getElementById('update_giaTriDonHangToiThieu').value = data.giaTriDonHangToiThieu || 0;
                    document.getElementById('update_ngayBatDau').value = data.ngayBatDau ? data.ngayBatDau.split('T')[0] : '';
                    document.getElementById('update_ngayKetThuc').value = data.ngayKetThuc ? data.ngayKetThuc.split('T')[0] : '';
                    document.getElementById('update_trangThai').value = data.trangThai || 2; // Mặc định là 2 (Chưa áp dụng)
                    
                    // Ẩn thông báo lỗi nếu có
                    document.getElementById('updateDiscountError').classList.add('d-none');
                })
                .catch(error => {
                    console.error('Error fetching discount:', error);
                    alert(error.message || 'Đã xảy ra lỗi khi lấy dữ liệu!');
                });
        });
    });

    // Xử lý submit form cập nhật khuyến mãi
    document.getElementById('updateDiscountForm')?.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const id = document.getElementById('update_id').value;
        console.log('Save update button clicked, ID:', id);
        
        if (!id || id === 'undefined' || id === 'null' || id === '') {
            alert('ID khuyến mãi không hợp lệ!');
            return;
        }
        
        const discountData = {
            maKM: document.getElementById('update_maKM').value,
            tenKM: document.getElementById('update_tenKM').value,
            giaTri: parseFloat(document.getElementById('update_giaTri').value),
            giaTriDonHangToiThieu: parseFloat(document.getElementById('update_giaTriDonHangToiThieu').value),
            ngayBatDau: document.getElementById('update_ngayBatDau').value,
            ngayKetThuc: document.getElementById('update_ngayKetThuc').value,
            trangThai: parseInt(document.getElementById('update_trangThai').value)
        };

        fetch(`/admin/discount/api/update/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(discountData)
        })
        .then(res => {
            if (res.ok) {
                alert('Cập nhật khuyến mãi thành công!');
                location.reload();
            } else {
                return res.text().then(msg => { 
                    document.getElementById('updateDiscountError').textContent = msg;
                    document.getElementById('updateDiscountError').classList.remove('d-none');
                    throw new Error(msg); 
                });
            }
        })
        .catch(error => {
            console.error('Error updating discount:', error);
        });
    });

    // Xử lý xóa khuyến mãi
    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.addEventListener('click', function() {
            const id = this.getAttribute('data-id');
            
            if (!id || id === 'undefined' || id === 'null') {
                alert('Không tìm thấy ID khuyến mãi!');
                return;
            }
            
            if (confirm('Bạn có chắc muốn xóa khuyến mãi này?')) {
                fetch(`/admin/discount/api/delete/${id}`, {
                    method: 'DELETE'
                })
                .then(res => {
                    if (res.ok) {
                        alert('Xóa khuyến mãi thành công!');
                        location.reload();
                    } else {
                        return res.text().then(msg => { throw new Error(msg); });
                    }
                })
                .catch(error => {
                    console.error('Error deleting discount:', error);
                    alert(error.message || 'Đã xảy ra lỗi khi xóa!');
                });
            }
        });
    });
});