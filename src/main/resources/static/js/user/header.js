 // Đợi cho toàn bộ trang được tải xong
document.addEventListener('DOMContentLoaded', function() {
                
    // Lấy các phần tử cần thiết bằng ID đã đặt
    const menuTrigger = document.getElementById('user-menu-trigger');
    const dropdownMenu = document.getElementById('user-dropdown-menu');

    // Chỉ thực hiện khi các phần tử tồn tại (tức là user đã đăng nhập)
    if (menuTrigger && dropdownMenu) {

        // 1. Xử lý khi click vào tên user
        menuTrigger.addEventListener('click', function(event) {
        event.preventDefault(); // Ngăn trình duyệt chuyển trang khi click vào thẻ <a>
        // Thêm hoặc xóa class 'show' để hiện/ẩn menu
        dropdownMenu.classList.toggle('show');
        });

        // 2. Xử lý khi click ra ngoài để đóng menu
        window.addEventListener('click', function(event) {
        // Kiểm tra xem cú click có nằm ngoài cả nút trigger và cả menu không
        if (!menuTrigger.contains(event.target) && !dropdownMenu.contains(event.target)) {
        // Nếu menu đang hiển thị thì ẩn đi
        if (dropdownMenu.classList.contains('show')) {
            dropdownMenu.classList.remove('show');
                    }
                }
            });
        }
});