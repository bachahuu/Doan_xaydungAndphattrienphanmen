document.addEventListener('DOMContentLoaded', function() {
    let isSubmitting = false; // Biến flag để tránh submit 2 lần

    // Xử lý mở modal sửa bài viết
    document.querySelectorAll('.btn-info').forEach(btn => {
        btn.addEventListener('click', function() {
            const id = this.getAttribute('data-article-id');
            console.log('Edit button clicked, ID:', id);
            
            if (!id || id === 'undefined' || id === 'null') {
                alert('ID bài viết không hợp lệ!');
                return;
            }
            
            // Reset flag khi mở modal
            isSubmitting = false;
            
            fetch(`/admin/posts/${id}`)
                .then(res => {
                    if (!res.ok) {
                        throw new Error('Không tìm thấy bài viết!');
                    }
                    return res.json();
                })
                .then(data => {
                    console.log('Article data:', data);
                    document.getElementById('edit_id').value = data.id || '';
                    document.getElementById('edit_maBaiViet').value = data.maBaiViet || '';
                    document.getElementById('edit_tieuDe').value = data.tieuDe || '';
                    document.getElementById('edit_noiDung').value = data.noiDung || '';
                    document.getElementById('edit_ngayDang').value = data.ngayDang ? data.ngayDang.split('T')[0] : '';
                    document.getElementById('edit_nhanVienId').value = data.nhanVienId || '';
                    document.getElementById('edit_hinhAnh').value = '';
                })
                .catch(error => {
                    console.error('Error fetching article:', error);
                    alert(error.message || 'Đã xảy ra lỗi khi lấy dữ liệu!');
                });
        });
    });

    // Xử lý click nút Lưu thay đổi
    const saveUpdateBtn = document.getElementById('saveUpdateArticleBtn');
    if (saveUpdateBtn) {
        saveUpdateBtn.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            
            // Ngăn submit nhiều lần
            if (isSubmitting) {
                console.log('Already submitting, please wait...');
                return;
            }
            
            const id = document.getElementById('edit_id').value;
            console.log('Save update button clicked, ID:', id);
            
            if (!id || id === 'undefined' || id === 'null' || id === '') {
                alert('ID bài viết không hợp lệ!');
                return;
            }
            
            isSubmitting = true;
            saveUpdateBtn.disabled = true;
            saveUpdateBtn.textContent = 'Đang lưu...';
            
            const formData = new FormData();
            formData.append('id', id);
            formData.append('maBaiViet', document.getElementById('edit_maBaiViet').value);
            formData.append('tieuDe', document.getElementById('edit_tieuDe').value);
            formData.append('noiDung', document.getElementById('edit_noiDung').value);
            formData.append('ngayDang', document.getElementById('edit_ngayDang').value);
            formData.append('nhanVienId', document.getElementById('edit_nhanVienId').value);
            
            const fileInput = document.getElementById('edit_hinhAnh');
            if (fileInput && fileInput.files.length > 0) {
                formData.append('hinhAnh', fileInput.files[0]);
                console.log('File selected:', fileInput.files[0].name);
            }

            fetch('/admin/posts/update', {
                method: 'PUT',
                body: formData
            })
            .then(res => {
                if (res.ok) {
                    alert('Cập nhật bài viết thành công!');
                    location.reload();
                } else {
                    return res.text().then(msg => { throw new Error(msg); });
                }
            })
            .catch(error => {
                console.error('Error updating article:', error);
                // alert(error.message || 'Đã xảy ra lỗi khi cập nhật!');
                isSubmitting = false;
                saveUpdateBtn.disabled = false;
                saveUpdateBtn.textContent = 'Lưu Thay Đổi';
            });
        });
    }

    // Xử lý click nút Lưu bài viết mới
    const saveNewBtn = document.getElementById('saveNewArticleBtn');
    if (saveNewBtn) {
        saveNewBtn.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            
            if (isSubmitting) {
                console.log('Already submitting, please wait...');
                return;
            }
            
            isSubmitting = true;
            saveNewBtn.disabled = true;
            saveNewBtn.textContent = 'Đang lưu...';
            
            const formData = new FormData();
            formData.append('maBaiViet', document.getElementById('add_maBaiViet').value);
            formData.append('tieuDe', document.getElementById('add_tieuDe').value);
            formData.append('noiDung', document.getElementById('add_noiDung').value);
            formData.append('ngayDang', document.getElementById('add_ngayDang').value);
            formData.append('nhanVienId', document.getElementById('add_nhanVienId').value);
            
            const fileInput = document.getElementById('add_hinhAnh');
            if (fileInput && fileInput.files.length > 0) {
                formData.append('hinhAnh', fileInput.files[0]);
                console.log('File selected:', fileInput.files[0].name);
            }

            fetch('/admin/posts/add', {
                method: 'POST',
                body: formData
            })
            .then(res => {
                if (res.ok) {
                    alert('Thêm bài viết thành công!');
                    location.reload();
                } else {
                    return res.text().then(msg => { throw new Error(msg); });
                }
            })
            .catch(error => {
                console.error('Error adding article:', error);
                // alert(error.message || 'Đã xảy ra lỗi khi thêm!');
                isSubmitting = false;
                saveNewBtn.disabled = false;
                saveNewBtn.textContent = 'Lưu bài viết';
            });
        });
    }

    // Xử lý xóa bài viết
    document.querySelectorAll('.btn-danger').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            
            const id = this.getAttribute('data-article-id');
            
            if (!id || id === 'undefined' || id === 'null') {
                alert('Không tìm thấy ID bài viết!');
                return;
            }
            
            if (confirm('Bạn có chắc muốn xóa bài viết này?')) {
                fetch(`/admin/posts/delete/${id}`, {
                    method: 'DELETE'
                })
                .then(res => {
                    if (res.ok) {
                        alert('Xóa bài viết thành công!');
                        location.reload();
                    } else {
                        return res.text().then(msg => { throw new Error(msg); });
                    }
                })
                .catch(error => {
                    console.error('Error deleting article:', error);
                    alert(error.message || 'Đã xảy ra lỗi khi xóa!');
                });
            }
        });
    });
});