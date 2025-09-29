document.addEventListener('DOMContentLoaded', function() {
    attachAddSupplierListener();
    attachSupplierEventListeners();
});

function attachSupplierEventListeners() {
    // Gán sự kiện cho nút sửa
    document.querySelectorAll('.btn-info').forEach(button => {
        button.addEventListener('click', function() {
            const supplierId = this.getAttribute('data-supplier-id');
            if (!supplierId) {
                alert('Không tìm thấy mã nhà cung cấp');
                return;
            }
            
            currentSupplierId = supplierId;
            
            // Lấy thông tin nhà cung cấp từ server
            fetch(`/api/admin/supplier/get/${supplierId}`)
                .then(response => {
                    if (!response.ok) throw new Error('Không tải được thông tin nhà cung cấp');
                    return response.json();
                })
                .then(supplier => {
                    // Điền thông tin vào form
                    document.getElementById('maNCC').value = supplier.maNCC;
                    document.getElementById('tenNCC').value = supplier.tenNCC;
                    document.getElementById('soDienThoai').value = supplier.soDienThoai;
                    document.getElementById('email').value = supplier.email;
                    document.getElementById('diaChi').value = supplier.diaChi;
                })
                .catch(error => {
                    alert(error.message);
                });
        });
    });

    // Gán sự kiện cho nút Lưu trong modal
    const updateBtn = document.getElementById('saveUpdateBtn');
    if (updateBtn) {
        updateBtn.addEventListener('click', function() {
                if (!currentSupplierId) {
                    alert('Không tìm thấy mã nhà cung cấp!');
                    return;
                }

                const data = {
                    maNCC: document.getElementById('maNCC').value.trim(),
                    tenNCC: document.getElementById('tenNCC').value.trim(),
                    soDienThoai: document.getElementById('soDienThoai').value.trim(),
                    email: document.getElementById('email').value.trim(),
                    diaChi: document.getElementById('diaChi').value.trim()
                };

                // Validate dữ liệu
                if (!data.tenNCC || !data.soDienThoai || !data.email || !data.diaChi) {
                    alert('Vui lòng điền đầy đủ thông tin!');
                    return;
                }

                // Validate số điện thoại (10 số)
                if (!/^[0-9]{10}$/.test(data.soDienThoai)) {
                    alert('Số điện thoại không hợp lệ (phải có 10 số)!');
                    return;
                }

                // Validate email
                if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(data.email)) {
                    alert('Email không hợp lệ!');
                    return;
                }

                // Gửi request cập nhật
                fetch(`/api/admin/supplier/update/${currentSupplierId}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(data)
                })
                .then(response => {
                    if (!response.ok) throw new Error('Cập nhật thất bại!');
                    alert('Cập nhật thành công!');
                                location.reload();
                })
                .catch(error => {
                    alert(error.message);
                });
        });
    }
    
    // Gán sự kiện cho nút xóa (chỉ nút xóa thực sự, không phải nút đóng modal)
    document.querySelectorAll('.btn-danger[data-supplier-id]').forEach(button => {
        button.addEventListener('click', function() {
            const supplierId = button.getAttribute('data-supplier-id');
            if (confirm('Bạn có chắc chắn muốn xóa nhà cung cấp này không?')) {
                fetch(`/api/admin/supplier/delete/${supplierId}`, {
                    method: 'DELETE'
                })
                .then(response => {
                    if (!response.ok) throw new Error('Xóa thất bại!');
                    if (confirm('Xóa thành công! Bạn có muốn tải lại danh sách không?')) {
                        window.location.href = window.location.href;
                    }
                })
                .catch(error => {
                    alert(error.message);
                });
            }
        });
    });
}

// Gán sự kiện cho form thêm nhà cung cấp
function attachAddSupplierListener() {
    const saveBtn = document.getElementById('saveNewSupplierBtn');
    if (saveBtn) {
        saveBtn.addEventListener('click', function() {
        const data = {
            maNCC: document.getElementById('addSupplierCode').value.trim(),
            tenNCC: document.getElementById('addSupplierName').value.trim(),
            soDienThoai: document.getElementById('addPhone').value.trim(),
            email: document.getElementById('addEmail').value.trim(),
            diaChi: document.getElementById('addAddress').value.trim()
        };

        // Validate dữ liệu
        if (!data.maNCC) {
            alert('Mã nhà cung cấp không được để trống!');
            return;
        }
        if (!data.tenNCC) {
            alert('Tên nhà cung cấp không được để trống!');
            return;
        }
        if (!data.soDienThoai) {
            alert('Số điện thoại không được để trống!');
            return;
        }
        if (!data.email) {
            alert('Email không được để trống!');
            return;
        }
        if (!data.diaChi) {
            alert('Địa chỉ không được để trống!');
            return;
        }

        // Validate số điện thoại (10 số)
        if (!/^[0-9]{10}$/.test(data.soDienThoai)) {
            alert('Số điện thoại không hợp lệ (phải có 10 số)!');
            return;
        }

        // Validate email
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(data.email)) {
            alert('Email không hợp lệ!');
            return;
        }

        // Gửi request thêm mới
        fetch('/api/admin/supplier/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => {
            if (!response.ok) throw new Error('Thêm mới thất bại!');
            return response.json();
        })
        .then(result => {
            if (confirm('Thêm nhà cung cấp thành công! Bạn có muốn tải lại danh sách không?')) {
                window.location.href = window.location.href;
            }
            // Đóng modal và reset form
            const modal = bootstrap.Modal.getInstance(document.getElementById('addSupplierModal'));
            if (modal) modal.hide();
            document.getElementById('addSupplierForm').reset();
        })
        .catch(error => {
            alert(error.message);
        });
        });
    }
}