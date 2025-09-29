document.addEventListener('DOMContentLoaded', function () {
    loadEmployeeList();
    attachAddEmployeeListener();
});

let currentEmployeeId = null; // Lưu mã NV đang sửa

// ==========================
// 1️⃣ Load danh sách nhân viên
// ==========================
function loadEmployeeList() {
    fetch('/admin/employee/api/list')
        .then(res => {
            if (!res.ok) throw new Error('Không tải được danh sách nhân viên');
            return res.json();
        })
        .then(data => {
            const tbody = document.querySelector('#employeeTable tbody');
            tbody.innerHTML = '';
            data.forEach((e, index) => {
                tbody.innerHTML += `
                    <tr style="text-align:center;">
                        <td>${index + 1}</td>
                        <td>${e.maNV}</td>
                        <td>${e.tenNV}</td>
                        <td>${e.soDienThoai}</td>
                        <td>${e.diaChi}</td>
                        <td>${e.chucVu}</td>
                        <td>
                            <div class="btn-group">
                                <button class="btn btn-sm btn-info btn-edit" 
                                    data-employee-id="${e.maNV}" 
                                    data-bs-toggle="modal" 
                                    data-bs-target="#editEmployeeModal">
                                    <i class="fas fa-edit"></i>
                                </button>
                                <button class="btn btn-sm btn-danger btn-delete" 
                                    data-employee-id="${e.maNV}">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </div>
                        </td>
                    </tr>
                `;
            });
            attachEmployeeEventListeners(); // Gắn sự kiện sau khi render
        })
        .catch(err => {
            console.error(err);
            alert('Không thể tải danh sách nhân viên!');
        });
}

// ==========================
// 2️⃣ Gắn sự kiện Sửa + Xóa
// ==========================
function attachEmployeeEventListeners() {
    // Sửa nhân viên
    document.querySelectorAll('.btn-edit').forEach(btn => {
        btn.addEventListener('click', () => {
            const maNV = btn.getAttribute('data-employee-id');
            currentEmployeeId = maNV;

            fetch(`/admin/employee/api/get/${maNV}`)
                .then(res => {
                    if (!res.ok) throw new Error('Không lấy được thông tin nhân viên');
                    return res.json();
                })
                .then(emp => {
                    document.getElementById('maNV').value = emp.maNV;
                    document.getElementById('tenNV').value = emp.tenNV;
                    document.getElementById('soDienThoai').value = emp.soDienThoai;
                    document.getElementById('diaChi').value = emp.diaChi;
                    document.getElementById('chucVu').value = emp.chucVu;
                })
                .catch(err => alert(err.message));
        });
    });

    // Nút lưu cập nhật
    document.getElementById('saveUpdateEmployeeBtn').onclick = function () {
        if (!currentEmployeeId) {
            alert('Không tìm thấy mã nhân viên!');
            return;
        }

        const data = {
            maNV: document.getElementById('maNV').value.trim(),
            tenNV: document.getElementById('tenNV').value.trim(),
            soDienThoai: document.getElementById('soDienThoai').value.trim(),
            diaChi: document.getElementById('diaChi').value.trim(),
            chucVu: document.getElementById('chucVu').value.trim()
        };

        if (!validateEmployee(data)) return;

        fetch(`/admin/employee/api/update/${currentEmployeeId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
            .then(res => {
                if (!res.ok) throw new Error('Cập nhật thất bại!');
                alert('Cập nhật thành công!');
                location.reload();
            })
            .catch(err => alert(err.message));
    };

    // Xóa nhân viên
    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.addEventListener('click', () => {
            const maNV = btn.getAttribute('data-employee-id');
            if (confirm('Bạn có chắc chắn muốn xóa nhân viên này không?')) {
                fetch(`/admin/employee/api/delete/${maNV}`, { method: 'DELETE' })
                    .then(res => {
                        if (!res.ok) throw new Error('Xóa thất bại!');
                        alert('Xóa thành công!');
                        loadEmployeeList();
                    })
                    .catch(err => alert(err.message));
            }
        });
    });
}

// ==========================
// 3️⃣ Gắn sự kiện Thêm mới
// ==========================
function attachAddEmployeeListener() {
    document.getElementById('saveNewEmployeeBtn').addEventListener('click', () => {
        const data = {
            maNV: document.getElementById('addEmployeeCode').value.trim(),
            tenNV: document.getElementById('addEmployeeName').value.trim(),
            soDienThoai: document.getElementById('addPhone').value.trim(),
            diaChi: document.getElementById('addAddress').value.trim(),
            chucVu: document.getElementById('addPosition').value.trim()
        };

        if (!validateEmployee(data, true)) return;

        fetch('/admin/employee/api/add', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
            .then(res => {
                if (!res.ok) throw new Error('Thêm nhân viên thất bại!');
                return res.json();
            })
            .then(() => {
                alert('Thêm nhân viên thành công!');
                const modal = bootstrap.Modal.getInstance(document.getElementById('addEmployeeModal'));
                if (modal) modal.hide();
                document.getElementById('addEmployeeForm').reset();
                loadEmployeeList();
            })
            .catch(err => alert(err.message));
    });
}

// ==========================
// 4️⃣ Validate dữ liệu
// ==========================
function validateEmployee(emp, isAdd = false) {
    if (isAdd && !emp.maNV) {
        alert('Mã nhân viên không được để trống!');
        return false;
    }
    if (!emp.tenNV || !emp.soDienThoai || !emp.diaChi || !emp.chucVu) {
        alert('Vui lòng điền đầy đủ thông tin!');
        return false;
    }
    if (!/^[0-9]{10}$/.test(emp.soDienThoai)) {
        alert('Số điện thoại phải có 10 chữ số!');
        return false;
    }
    return true;
}
