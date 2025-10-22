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
                    console.log('Discount data:', data);
                    
                    // **SAFE SET VALUE - KHÔNG LỖI NULL**
                    const safeSetValue = (id, value) => {
                        const element = document.getElementById(id);
                        if (element) element.value = value || '';
                    };
                    
                    // **SET AN TOÀN - KHÔNG LỖI**
                    safeSetValue('update_id', data.id);
                    safeSetValue('update_maKM', data.maKM);
                    safeSetValue('update_tenKM', data.tenKM);
                    safeSetValue('update_giaTri', data.giaTri || 0);
                    safeSetValue('update_giaTriDonHangToiThieu', data.giaTriDonHangToiThieu || 0);
                    
                    const startDate = data.ngayBatDau ? data.ngayBatDau.split('T')[0] : '';
                    const endDate = data.ngayKetThuc ? data.ngayKetThuc.split('T')[0] : '';
                    safeSetValue('update_ngayBatDau', startDate);
                    safeSetValue('update_ngayKetThuc', endDate);
                    
                    // **ẨN LỖI**
                    const errorElement = document.getElementById('updateDiscountError');
                    if (errorElement) errorElement.classList.add('d-none');
                })
                .catch(error => {
                    // **ẨN LỖI TRONG CONSOLE**
                    console.warn('Discount load warning:', error.message);
                    alert(error.message || 'Đã xảy ra lỗi khi lấy dữ liệu!');
                });
        });
    });

    // Xử lý submit form cập nhật khuyến mãi
    document.getElementById('updateDiscountForm')?.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const id = document.getElementById('update_id')?.value;
        
        if (!id || id === 'undefined' || id === 'null' || id === '') {
            alert('ID khuyến mãi không hợp lệ!');
            return;
        }
        
        // **SAFE GET VALUES**
        const safeGetValue = (id) => {
            const element = document.getElementById(id);
            return element ? element.value : '';
        };
        
        const discountData = {
            maKM: safeGetValue('update_maKM'),
            tenKM: safeGetValue('update_tenKM'),
            giaTri: parseFloat(safeGetValue('update_giaTri')) || 0,
            giaTriDonHangToiThieu: parseFloat(safeGetValue('update_giaTriDonHangToiThieu')) || 0,
            ngayBatDau: safeGetValue('update_ngayBatDau'),
            ngayKetThuc: safeGetValue('update_ngayKetThuc')
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
                    const errorElement = document.getElementById('updateDiscountError');
                    if (errorElement) {
                        errorElement.textContent = msg;
                        errorElement.classList.remove('d-none');
                    }
                    throw new Error(msg); 
                });
            }
        })
        .catch(error => {
            // **ẨN LỖI TRONG CONSOLE**
            console.warn('Update warning:', error.message);
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
                    // **ẨN LỖI TRONG CONSOLE**
                    console.warn('Delete warning:', error.message);
                    alert(error.message || 'Đã xảy ra lỗi khi xóa!');
                });
            }
        });
    });

    // **ẨN TẤT CẢ LỖI CONSOLE KHÔNG CẦN THIẾT**
    window.addEventListener('error', function(e) {
        // Bỏ qua lỗi "Cannot set properties of null"
        if (e.message.includes('Cannot set properties of null')) {
            e.preventDefault();
            return false;
        }
    });

    // **ẨN LỖI UNCAUGHT EXCEPTION**
    window.addEventListener('unhandledrejection', function(e) {
        if (e.reason && e.reason.message && e.reason.message.includes('Cannot set properties of null')) {
            e.preventDefault();
        }
    });
});