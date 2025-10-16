document.addEventListener("DOMContentLoaded", function () {
    const currentPath = window.location.pathname;

    function updateActiveState() {
        // Bỏ active hiện có
        document.querySelectorAll(".nav-link.active, .sub-nav-link.active").forEach(link => link.classList.remove("active"));
        document.querySelectorAll(".sub-menu.show").forEach(menu => menu.classList.remove("show"));

        // Xác định sub-link trùng với đường dẫn hiện tại
        const subLinks = document.querySelectorAll(".sub-nav-link");
        let activeSubLink = null;
        subLinks.forEach(link => {
            if (link.getAttribute("href") === currentPath) {
                activeSubLink = link;
            }
        });

        if (activeSubLink) {
            activeSubLink.classList.add("active");
            const parentMenu = activeSubLink.closest(".sub-menu");
            if (parentMenu) {
                parentMenu.classList.add("show");
                const parentNav = parentMenu.previousElementSibling;
                if (parentNav) parentNav.classList.add("active");
            }
        } else {
            // Nếu không có sub-link khớp thì active nav-link chính
            document.querySelectorAll(".nav-link").forEach(link => {
                if (link.getAttribute("href") === currentPath) link.classList.add("active");
            });
        }
    }

    updateActiveState();

    // Xử lý click cho sub-link
    document.querySelectorAll(".sub-nav-link").forEach(link => {
        link.addEventListener("click", function (e) {
            const subMenu = this.closest(".sub-menu");
            if (subMenu) {
                subMenu.classList.add("show");
                this.classList.add("active");
                const parentMenu = subMenu.previousElementSibling;
                if (parentMenu) parentMenu.classList.add("active");
            }
        });
    });

    // Xử lý click mở/đóng nav-link và xoay mũi tên
    document.querySelectorAll(".nav-link").forEach(link => {
        link.addEventListener("click", function (e) {
            const subMenu = this.nextElementSibling;
            if (subMenu && subMenu.classList.contains("sub-menu")) {
                e.preventDefault();
                const isOpen = subMenu.classList.contains("show");

                // Đóng tất cả menu khác
                document.querySelectorAll(".sub-menu.show").forEach(menu => {
                    if (menu !== subMenu) {
                        menu.classList.remove("show");
                        const navLink = menu.previousElementSibling;
                        if (navLink) navLink.classList.remove("active");
                        const arrow = navLink.querySelector(".arrow");
                        if (arrow) arrow.classList.remove("rotate");
                    }
                });

                // Mở menu hiện tại
                subMenu.classList.toggle("show", !isOpen);
                this.classList.toggle("active", !isOpen);

                // Xoay mũi tên
                const arrow = this.querySelector(".arrow");
                if (arrow) arrow.classList.toggle("rotate", !isOpen);
            }
        });
    });
});
