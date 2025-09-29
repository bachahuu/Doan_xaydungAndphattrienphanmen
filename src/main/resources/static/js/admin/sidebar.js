document.addEventListener("DOMContentLoaded", function() {
    // Lấy đường dẫn hiện tại
    const currentPath = window.location.pathname;

    // Hàm để cập nhật trạng thái active
    function updateActiveState() {
        const allActiveLinks = document.querySelectorAll(".nav-link.active, .sub-nav-link.active");
        allActiveLinks.forEach(link => link.classList.remove("active"));

        const allSubMenus = document.querySelectorAll(".sub-menu.show");
        allSubMenus.forEach(menu => menu.classList.remove("show"));

        // Tìm và active sub-link dựa trên URL
        const subLinks = document.querySelectorAll(".sub-nav-link");
        let activeSubLink = null;
        subLinks.forEach(link => {
            if (link.getAttribute("href") === currentPath) {
                activeSubLink = link;
            }
        });

        if (activeSubLink) {
            activeSubLink.classList.add("active");
            const parentMenu = activeSubLink.closest(".sub-menu").previousElementSibling;
            if (parentMenu) {
                parentMenu.classList.add("active");
                activeSubLink.closest(".sub-menu").classList.add("show");
            }
        } else {
            // Kiểm tra active cho nav-link cha nếu không có sub-link khớp
            const navLinks = document.querySelectorAll(".nav-link");
            navLinks.forEach(link => {
                if (link.getAttribute("href") === currentPath) {
                    link.classList.add("active");
                }
            });
        }
    }

    // Gọi hàm cập nhật khi trang load
    updateActiveState();

    // Xử lý sự kiện click để giữ menu mở và cập nhật active
    const subLinks = document.querySelectorAll(".sub-nav-link");
    subLinks.forEach(link => {
        link.addEventListener("click", function(e) {
            e.preventDefault(); // Ngăn chặn hành vi mặc định nếu cần
            const subMenu = this.closest(".sub-menu");
            if (subMenu) {
                subMenu.classList.add("show"); // Giữ menu mở
                this.classList.add("active"); // Active sub-link được click
                const parentMenu = subMenu.previousElementSibling;
                if (parentMenu) {
                    parentMenu.classList.add("active"); // Active menu cha
                }
            }
            // Cập nhật URL (nếu cần điều hướng)
            if (this.getAttribute("href")) {
                window.location.href = this.getAttribute("href");
            }
        });
    });

    // Xử lý sự kiện click cho nav-link để mở/đóng menu
    const navLinks = document.querySelectorAll(".nav-link");
    navLinks.forEach(link => {
        link.addEventListener("click", function(e) {
            const subMenu = this.nextElementSibling;
            if (subMenu && subMenu.classList.contains("sub-menu")) {
                e.preventDefault(); // Ngăn chặn điều hướng nếu chỉ mở/đóng
                const isOpen = subMenu.classList.contains("show");
                if (!isOpen) {
                    subMenu.classList.add("show");
                    this.classList.add("active");
                } else {
                    subMenu.classList.remove("show");
                    this.classList.remove("active");
                }
            }
        });
    });
});