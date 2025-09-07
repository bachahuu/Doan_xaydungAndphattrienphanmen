document.addEventListener('DOMContentLoaded', function() {
    // Lấy tất cả các nút "Update"
    const updateButtons = document.querySelectorAll('.btn-update');

    updateButtons.forEach(button => {
        button.addEventListener('click', function() {
            // Lấy giá trị từ thuộc tính data-
            const maKhuyenMai = this.getAttribute('data-ma_khuyen_mai');
            const tenKhuyenMai = this.getAttribute('data-ten_khuyen_mai');
            const giamGia = this.getAttribute('data-giam_gia');
            const ngayBatDau = this.getAttribute('data-ngay_bat_dau');
            const ngayKetThuc = this.getAttribute('data-ngay_ket_thuc');
            const soLuong = this.getAttribute('data-so_luong');

            // Điền giá trị vào các trường trong modal
            document.getElementById('update_maCTKhuyenMai').value = maKhuyenMai;
            document.getElementById('update_tenChuongTrinh').value = tenKhuyenMai;
            document.getElementById('update_giamGia').value = giamGia;
            document.getElementById('update_ngayBatDau').value = ngayBatDau;
            document.getElementById('update_ngayKetThuc').value = ngayKetThuc;
            document.getElementById('update_soLuong').value = soLuong;
        });
    });
});