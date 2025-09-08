document.addEventListener('DOMContentLoaded', function () {
    // Function to update totals from localStorage
    function updateTotalsFromLocalStorage() {
        const stored = localStorage.getItem('cartTotalPrice');
        const cartTotal = stored ? parseInt(stored) : 0;

        const ship = 30000; // Default shipping fee
        const discount = 0; // Default discount
        const tamTinh = Math.max(0, cartTotal - discount);
        const total = tamTinh + ship;

        document.getElementById('cartTotalPriceInput').value = cartTotal;
        document.getElementById('total-display').innerText = total;
    }

    // Initialize totals
    updateTotalsFromLocalStorage();

    // Get the button and modal
    const completePayButton = document.getElementById('complete_pay');
    const successModal = document.getElementById('successModal');
    const checkoutForm = document.getElementById('checkoutForm');

    // Initialize Bootstrap modal
    const bootstrapModal = new bootstrap.Modal(successModal);

    // Handle "Xác nhận thanh toán" button click
    if (completePayButton) {
        completePayButton.addEventListener('click', function () {
            // Check if a payment method is selected
            const selectedPaymentMethod = document.querySelector('input[name="paymentMethod"]:checked');
            if (!selectedPaymentMethod) {
                alert('Vui lòng chọn phương thức thanh toán!');
                return;
            }

            // Update hidden form input with selected payment method
            document.getElementById('selectedPaymentMethod').value = selectedPaymentMethod.value;

            // Show the success modal
            bootstrapModal.show();

            // Optional: Submit the form after showing the modal
            // checkoutForm.submit(); // Uncomment if you want immediate form submission
        });
    }

    // Handle form submission
    if (checkoutForm) {
        checkoutForm.addEventListener('submit', function (e) {
            const radios = document.getElementsByName('paymentMethod');
            let checked = null;
            for (let i = 0; i < radios.length; i++) {
                if (radios[i].checked) {
                    checked = radios[i].value;
                    break;
                }
            }
            if (!checked) {
                e.preventDefault();
                alert('Vui lòng chọn một phương thức thanh toán.');
                return false;
            }
            document.getElementById('selectedPaymentMethod').value = checked;
        });
    }

    // Handle "Quay lại Giỏ Hàng" button in the modal
    const backToCartBtn = document.getElementById('backToCartBtn');
    if (backToCartBtn) {
        backToCartBtn.addEventListener('click', function (e) {
            e.preventDefault();
            window.location.href = '/cart'; // Adjust to your cart page URL
        });
    }

    // Handle "Đóng" button in the modal
    if (closeModalButton) {
        closeModalButton.addEventListener('click', function () {
            bootstrapModal.hide(); // Programmatically hide the modal
        });
    }
});