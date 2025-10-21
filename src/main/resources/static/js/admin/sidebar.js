document.addEventListener("DOMContentLoaded", function () {
    const currentPath = window.location.pathname;

    /**
     * Cập nhật trạng thái active cho menu dựa trên URL hiện tại.
     */
    function updateActiveState() {
        // Bỏ active hiện có
        document.querySelectorAll(".nav-link.active, .sub-nav-link.active").forEach(link => {
            link.classList.remove("active");
            link.setAttribute("aria-expanded", "false");
        });
        document.querySelectorAll(".sub-menu.show").forEach(menu => menu.classList.remove("show"));

        // Tìm sub-link khớp với đường dẫn
        const subLinks = document.querySelectorAll(".sub-nav-link");
        let activeSubLink = null;
        subLinks.forEach(link => {
            if (link.getAttribute("href") === currentPath) {
                activeSubLink = link;
            }
        });

        if (activeSubLink) {
            // Nếu tìm thấy sub-link, active nó và menu cha
            activeSubLink.classList.add("active");
            const parentMenu = activeSubLink.closest(".sub-menu");
            if (parentMenu) {
                parentMenu.classList.add("show");
                const parentNav = parentMenu.previousElementSibling;
                if (parentNav && parentNav.classList.contains("nav-link")) {
                    parentNav.classList.add("active");
                    parentNav.setAttribute("aria-expanded", "true");
                }
            }
        } else {
            // Nếu không, chỉ active nav-link chính (nếu khớp)
            document.querySelectorAll(".nav-link").forEach(link => {
                if (link.getAttribute("href") === currentPath) {
                    link.classList.add("active");
                    link.setAttribute("aria-expanded", "true");
                }
            });
        }
    }

    // Chạy khi tải trang
    updateActiveState();

    /**
     * Xử lý click cho các menu cha (nav-link)
     * Đây là phần logic chính để Đóng/Mở
     */
    document.querySelectorAll(".nav-link").forEach(link => {
        // Khởi tạo aria-expanded cho tất cả
        if (!link.hasAttribute("aria-expanded")) {
            link.setAttribute("aria-expanded", "false");
        }
        
        link.addEventListener("click", function (e) {
            const subMenu = this.nextElementSibling;
            
            // Chỉ xử lý nếu có sub-menu
            if (subMenu && subMenu.classList.contains("sub-menu")) {
                e.preventDefault(); // Ngăn chuyển trang (vì href="#")
                
                // Kiểm tra trạng thái của menu *đang được nhấp*
                const isCurrentlyOpen = subMenu.classList.contains("show");

                // --- Logic Accordion: Đóng tất cả menu khác ---
                document.querySelectorAll(".sub-menu.show").forEach(menu => {
                    if (menu !== subMenu) { // Chỉ đóng nếu là menu khác
                        menu.classList.remove("show");
                        const navLink = menu.previousElementSibling;
                        if (navLink) {
                            navLink.classList.remove("active");
                            navLink.setAttribute("aria-expanded", "false");
                        }
                    }
                });

                // --- Logic Toggle: Đóng/Mở menu hiện tại ---
                if (isCurrentlyOpen) {
                    // Nếu đang mở -> Đóng nó lại
                    subMenu.classList.remove("show");
                    this.classList.remove("active");
                    this.setAttribute("aria-expanded", "false");
                } else {
                    // Nếu đang đóng -> Mở nó ra
                    subMenu.classList.add("show");
                    this.classList.add("active");
                    this.setAttribute("aria-expanded", "true");
                }
            }
        });
    });
});