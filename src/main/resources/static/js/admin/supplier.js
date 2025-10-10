let editSupplierModal = null;
let addSupplierModal = null;
let currentSupplierId = null;


// hàm load thônng tin nhá cung cấp lên form sửa
function showEditSupplier(maNCC) {
    if (!maNCC) {
        alert('Không tìm thấy mã nhà cung cấp');
        return;
    }
    
    currentSupplierId = maNCC;
    
    // Lấy thông tin nhà cung cấp từ server
    fetch(`/api/admin/supplier/get/${maNCC}`)
        .then(response => {
            if (!response.ok) throw new Error('Không thể tải thông tin nhà cung cấp');
            return response.json();
        })
        .then(supplier => {
            // Điền thông tin vào form
            document.getElementById('maNCC').value = supplier.maNCC || '';
            document.getElementById('tenNCC').value = supplier.tenNCC || '';
            document.getElementById('soDienThoai').value = supplier.soDienThoai || '';
            document.getElementById('email').value = supplier.email || '';
            document.getElementById('diaChi').value = supplier.diaChi || '';
            
            // Hiển thị modal
            editSupplierModal.show();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Lỗi: ' + error.message);
        });
}

// hàm cập nhật thông tin nhà cung cấp
function handleUpdateSupplier() {
    if (!currentSupplierId) {
        alert('Không tìm thấy mã nhà cung cấp!');
        return;
    }

    const updateData = {
        tenNCC: document.getElementById('tenNCC').value.trim(),
        soDienThoai: document.getElementById('soDienThoai').value.trim(),
        email: document.getElementById('email').value.trim(),
        diaChi: document.getElementById('diaChi').value.trim()
    };

    // Validate dữ liệu
    if (!updateData.tenNCC || !updateData.soDienThoai || !updateData.email || !updateData.diaChi) {
        alert('Vui lòng điền đầy đủ thông tin!');
        return;
    }

    // Validate số điện thoại (10 số)
    if (!/^[0-9]{10}$/.test(updateData.soDienThoai)) {
        alert('Số điện thoại không hợp lệ (phải có 10 số)!');
        return;
    }

    // Validate email
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(updateData.email)) {
        alert('Email không hợp lệ!');
        return;
    }

    // Gửi request cập nhật
    fetch(`/api/admin/supplier/update/${currentSupplierId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updateData)
    })
    .then(response => {
        if (response.ok) {
            alert('Cập nhật nhà cung cấp thành công!');
            editSupplierModal.hide();
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

// hàm xử lý thêm mới nhà cung cấp
function handleAddSupplier() {
    const addData = {
        maNCC: document.getElementById('addSupplierCode').value.trim(),
        tenNCC: document.getElementById('addSupplierName').value.trim(),
        soDienThoai: document.getElementById('addPhone').value.trim(),
        email: document.getElementById('addEmail').value.trim(),
        diaChi: document.getElementById('addAddress').value.trim()
    };

    // Validate dữ liệu
    if (!addData.maNCC) {
        alert('Mã nhà cung cấp không được để trống!');
        return;
    }
    if (!addData.tenNCC) {
        alert('Tên nhà cung cấp không được để trống!');
        return;
    }
    if (!addData.soDienThoai) {
        alert('Số điện thoại không được để trống!');
        return;
    }
    if (!addData.email) {
        alert('Email không được để trống!');
        return;
    }
    if (!addData.diaChi) {
        alert('Địa chỉ không được để trống!');
        return;
    }

    // Validate số điện thoại (10 số)
    if (!/^[0-9]{10}$/.test(addData.soDienThoai)) {
        alert('Số điện thoại không hợp lệ (phải có 10 số)!');
        return;
    }

    // Validate email
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(addData.email)) {
        alert('Email không hợp lệ!');
        return;
    }

    // Gửi request thêm mới
    fetch('/api/admin/supplier/add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(addData)
    })
    .then(response => {
        if (response.ok) {
            alert('Thêm nhà cung cấp thành công!');
            addSupplierModal.hide();
            
            // Reset form
            const form = document.getElementById('addSupplierForm');
            if (form) form.reset();
            
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

//hàm xử lý xóa nhà cung cấp
function handleDeleteSupplier(maNCC) {
    if (!maNCC) {
        alert('Không tìm thấy mã nhà cung cấp!');
        return;
    }

    if (confirm('Bạn có chắc chắn muốn xóa nhà cung cấp này không?')) {
        fetch(`/api/admin/supplier/delete/${maNCC}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                alert('Xóa nhà cung cấp thành công!');
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
}

// khởi tạo submit sự kiện khi DOM load xong
function initEditSupplierFormSubmit() {
    const saveUpdateBtn = document.getElementById('saveUpdateBtn');
    if (saveUpdateBtn) {
        saveUpdateBtn.addEventListener('click', function(event) {
            event.preventDefault();
            handleUpdateSupplier();
        });
    }
}
function initAddSupplierFormSubmit() {
    const saveNewSupplierBtn = document.getElementById('saveNewSupplierBtn');
    if (saveNewSupplierBtn) {
        saveNewSupplierBtn.addEventListener('click', function(event) {
            event.preventDefault();
            handleAddSupplier();
        });
    }
}


document.addEventListener('DOMContentLoaded', function() {
    // Khởi tạo modal
    const editModalElement = document.getElementById('editSupplierModal');
    const addModalElement = document.getElementById('addSupplierModal');
    if (editModalElement) {
        editSupplierModal = new bootstrap.Modal(editModalElement);
    }
    if (addModalElement) {
        addSupplierModal = new bootstrap.Modal(addModalElement);
    }
    
    // Gán sự kiện cho form
    initEditSupplierFormSubmit();
    initAddSupplierFormSubmit();
});
