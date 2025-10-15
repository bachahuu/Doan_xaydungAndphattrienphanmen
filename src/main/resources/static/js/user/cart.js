function deleteCartItem(cartId) {
    if (confirm("Bạn chắc chắn xoá chứ ?")) {
        fetch(`/api/cart/detail/${cartId}`,{
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                alert('Xoá sản phẩm khỏi giỏ hàng thành công!');
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
// Cập nhật số lượng của từng sản phẩm trong giỏ hàng
function updateQuantity(chiTietId, soLuong) {
    fetch(`/api/cart/detail/${chiTietId}?soLuong=${soLuong}`, {
        method: 'PUT'
    })
    .then(response => {
        if (!response.ok) throw new Error('Cập nhật số lượng thất bại');
        return response.json();
    })
    .then(total => {
        if (total == -1) return; // backend báo cần hỏi xoá, không xử lý tiếp
        const formatted = new Intl.NumberFormat('vi-VN').format(total);
        const priceEl = document.querySelector(`[data-price-id="${chiTietId}"]`);
        if (priceEl) priceEl.textContent = formatted;
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Lỗi: ' + error.message);
    });
}
//hàm tính lại tổng số mặt hàng  trong giỏ hàng
function updateCartSummary() {
    let totalItems = 0;
    let totalPrice = 0;

    document.querySelectorAll('input[data-detail-id]').forEach(input => {
        const quantity = parseInt(input.value) || 0;
        const price = parseFloat(input.getAttribute('data-price')) || 0;
        if(quantity > 0) totalItems++;
        totalPrice += quantity * price;
    });
    document.getElementById('total-products').textContent = totalItems;
    document.getElementById('subtotal').textContent = totalPrice.toLocaleString('vi-VN') + 'đ';
    
    
}

// hàm thêm sản phẩm vào giỏ hàng
function addToCart(event) {
    if (event && event.preventDefault) event.preventDefault();

    // try to find the form or inputs
    let form = null;
    if (event && event.target && event.target.tagName === 'FORM') form = event.target;
    if (!form) form = document.getElementById('addToCartForm');

    const productIdEl = form ? form.querySelector('input[name="productId"]') : document.querySelector('input[name="productId"]');
    const quantityEl = form ? form.querySelector('input[name="quantity"]') : document.querySelector('input[name="quantity"]');

    if (!productIdEl || !quantityEl) {
        alert('Không tìm thấy thông tin sản phẩm để thêm vào giỏ hàng.');
        return;
    }

    const productId = productIdEl.value;
    const quantity = quantityEl.value || 1;

    fetch('/api/cart/add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
        body: new URLSearchParams({ productId: productId, quantity: quantity })
    })
    .then(response => {
        if (!response.ok) return response.text().then(t => { throw new Error(t || 'Thêm vào giỏ hàng thất bại'); });
        return response.json();
    })
    .then(data => {
        // success: data should be the added cartDetailEntity
        if (confirm('Đã thêm vào giỏ hàng. Xem giỏ hàng bây giờ?')) {
            window.location.href = '/cart';
        } else {
            // update cart summary (if product list present)
            try { updateCartSummary(); } catch (e) { /* ignore */ }
        }
    })
    .catch(err => {
        console.error('Add to cart error:', err);
        alert('Lỗi khi thêm vào giỏ hàng: ' + err.message);
    });
}

//kiểm tra giỏ hàng trống
function diachi() {
    fetch('/api/cart/check-empty')
        .then(response => {
            if (!response.ok) {
                return response.text().then(msg => { throw new Error(msg); });
            }
            return response.text();
        })
        .then(msg => {
            console.log(msg); // log nếu muốn
            window.location.href = "/location"; // ✅ chỉ chuyển khi hợp lệ
        })
        .catch(err => {
            alert(err.message || 'Giỏ hàng của bạn đang trống. Vui lòng thêm sản phẩm trước khi thanh toán.');
        });
}

document.addEventListener('DOMContentLoaded', function(){

    // Cập nhật tổng ban đầu khi trang vừa load
    updateCartSummary();

    //sự kiện thay đổi số lượng sản phẩm trong giỏ hàng
    document.querySelectorAll('input[type="number"]').forEach(input => {
        input.addEventListener('change', function() {
            const chiTietId = this.getAttribute('data-detail-id');
            const soLuong = parseInt(this.value);

            if(soLuong < 1) {
                if(confirm("Số lượng không hợp lệ. Bạn có muốn xoá sản phẩm khỏi giỏ hàng không?")) {
                    deleteCartItem(chiTietId);
                }else{
                    this.value = 1; // Đặt lại số lượng về 1 nếu người dùng không muốn xoá
                }
                return;
            }
            //
            updateQuantity(chiTietId, soLuong);
        });
    });

    //sự kiện xoá sản phẩm trong giỏ hàng
    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const id = this.getAttribute('data-id');
            deleteCartItem(id);// xoá sản phẩm trong giỏ hàng
        });
    });

    // Wire add-to-cart form submit to addToCart function if present
    const addToCartForm = document.getElementById('addToCartForm');
    if (addToCartForm) {
        addToCartForm.addEventListener('submit', addToCart);
    }
});