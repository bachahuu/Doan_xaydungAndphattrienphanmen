document.addEventListener('DOMContentLoaded', function() {
    const apiBase = window.location.origin + '/api/admin/danhmuc';

    function fetchDanhMucList() {
        fetch(apiBase + '/list')
            .then(res => res.json())
            .then(data => renderTable(data))
            .catch(err => console.error('Lỗi khi tải danh mục', err));
    }

    function renderTable(list) {
        const tbody = document.querySelector('#danhMucTable tbody');
        if (!tbody) return;
        tbody.innerHTML = '';
        list.forEach((dm, idx) => {
            const tr = document.createElement('tr');
            tr.style.textAlign = 'center';
            tr.innerHTML = `
                <td>${idx + 1}</td>
                <td>${dm.maDanhMuc || ''}</td>
                <td>${dm.tenDanhMuc || ''}</td>
                <td>${dm.moTa || ''}</td>
                <td>
                    <div class="btn-group">
                        <button class="btn btn-sm btn-info" data-bs-toggle="modal" data-bs-target="#editDanhMucModal" 
                            data-madanhmuc="${dm.maDanhMuc}" data-tendanhmuc="${dm.tenDanhMuc}" data-mota="${dm.moTa}">Sửa</button>
                        <button class="btn btn-sm btn-danger" data-madanhmuc="${dm.maDanhMuc}">Xóa</button>
                    </div>
                </td>
            `;
            tbody.appendChild(tr);
        });

        // Attach click handlers
        document.querySelectorAll('#danhMucTable .btn-info').forEach(btn => {
            btn.addEventListener('click', function(e) {
                const code = this.getAttribute('data-madanhmuc');
                document.getElementById('maDanhMuc').value = code;
                document.getElementById('tenDanhMuc').value = this.getAttribute('data-tendanhmuc') || '';
                document.getElementById('moTa').value = this.getAttribute('data-mota') || '';
            });
        });

        document.querySelectorAll('#danhMucTable .btn-danger').forEach(btn => {
            btn.addEventListener('click', function(e) {
                const code = (this.getAttribute('data-madanhmuc') || '').trim();
                if (!code) {
                    alert('Mã danh mục không hợp lệ hoặc bị rỗng. Không thể xóa.');
                    return;
                }
                if (confirm('Bạn có chắc muốn xóa danh mục ' + code + ' ?')) {
                    const url = apiBase + '/delete/' + encodeURIComponent(code);
                    console.log('DELETE', url);
                    fetch(url, { method: 'DELETE' })
                    .then(res => {
                        if (!res.ok) return res.text().then(t => { throw new Error(t) });
                        fetchDanhMucList();
                    })
                    .catch(err => alert('Lỗi khi xóa: ' + err.message));
                }
            });
        });
    }

    // Save update
    const saveBtn = document.getElementById('saveDanhMucBtn');
    if (saveBtn) {
        saveBtn.addEventListener('click', function() {
            const ma = (document.getElementById('maDanhMuc').value || '').trim();
            const ten = (document.getElementById('tenDanhMuc').value || '').trim();
            const mota = (document.getElementById('moTa').value || '').trim();

            if (!ma) { alert('Mã danh mục không hợp lệ'); return; }
            if (!ten) { alert('Tên danh mục không được để trống'); return; }

            const url = apiBase + '/update/' + encodeURIComponent(ma);
            console.log('PUT', url);
            fetch(url, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ tenDanhMuc: ten, moTa: mota })
            })
            .then(res => {
                if (!res.ok) return res.text().then(t => { throw new Error(t) });
                return res.json();
            })
            .then(() => {
                const modalEl = document.getElementById('editDanhMucModal');
                const modal = bootstrap.Modal.getInstance(modalEl);
                modal.hide();
                fetchDanhMucList();
            })
            .catch(err => alert('Lỗi khi cập nhật: ' + err.message));
        });
    }

    // Save new
    const saveNewBtn = document.getElementById('saveNewDanhMucBtn');
    if (saveNewBtn) {
        saveNewBtn.addEventListener('click', function() {
            const ma = (document.getElementById('addMaDanhMuc').value || '').trim();
            const ten = (document.getElementById('addTenDanhMuc').value || '').trim();
            const mota = (document.getElementById('addMoTa').value || '').trim();

            // client-side validation
            if (!ma) { alert('Mã danh mục không được để trống'); return; }
            if (!ten) { alert('Tên danh mục không được để trống'); return; }

            const url = apiBase + '/add';
            console.log('POST', url);
            fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ maDanhMuc: ma, tenDanhMuc: ten, moTa: mota })
            })
            .then(res => {
                if (!res.ok) return res.text().then(t => { throw new Error(t) });
                return res.json();
            })
            .then(() => {
                const modalEl = document.getElementById('addDanhMucModal');
                const modal = bootstrap.Modal.getInstance(modalEl);
                modal.hide();
                // reset form
                document.getElementById('addDanhMucForm').reset();
                fetchDanhMucList();
            })
            .catch(err => alert('Lỗi khi thêm mới: ' + err.message));
        });
    }

    // expose delete function in case any inline calls remain
    window.deleteDanhMuc = function(btnOrCode) {
        let code = null;
        if (typeof btnOrCode === 'string') code = btnOrCode;
        else if (btnOrCode && btnOrCode.getAttribute) code = btnOrCode.getAttribute('data-madanhmuc');
        if (!code) { alert('Không tìm thấy mã danh mục để xóa'); return; }
        if (!confirm('Bạn có chắc muốn xóa danh mục ' + code + ' ?')) return;
        fetch(apiBase + '/delete/' + encodeURIComponent(code), { method: 'DELETE' })
            .then(res => {
                if (!res.ok) return res.text().then(t => { throw new Error(t) });
                fetchDanhMucList();
            })
            .catch(err => alert('Lỗi khi xóa: ' + err.message));
    }

    // initial load
    fetchDanhMucList();
});
