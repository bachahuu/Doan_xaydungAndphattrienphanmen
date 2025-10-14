$(document).ready(function() {
    console.log('jQuery loaded and document ready');
    if ($('#addAccountForm').length === 0) {
        console.error('Form #addAccountForm not found in DOM');
    }
    if ($('#saveNewAccountBtn').length === 0) {
        console.error('Button #saveNewAccountBtn not found in DOM');
    }

    // Xử lý sự kiện submit của form thêm tài khoản
    $('#addAccountForm').on('submit', function(event) {
        event.preventDefault();
        console.log('Form #addAccountForm submitted');
        var username = $('#add_username').val().trim();
        var password = $('#add_password').val().trim();
        var role = $('#add_role').val();
        console.log('Input data:', { username: username, password: password, role: role });
        if (!username) {
            console.log('Validation failed: Username is empty');
            alert('Vui lòng nhập tên đăng nhập');
            return;
        }
        if (!password) {
            console.log('Validation failed: Password is empty');
            alert('Vui lòng nhập mật khẩu');
            return;
        }
        if (password.length < 6) {
            console.log('Validation failed: Password too short');
            alert('Mật khẩu phải có ít nhất 6 ký tự');
            return;
        }
        if (!role) {
            console.log('Validation failed: Role not selected');
            alert('Vui lòng chọn quyền');
            return;
        }
        var account = {
            username: username,
            password: password,
            role: role
        };
        console.log('Sending AJAX request with data:', account);
        $.ajax({
            url: '/admin/account/add',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(account),
            success: function(response) {
                console.log('AJAX success:', response);
                alert('Thêm tài khoản thành công');
                $('#addAccountModal').modal('hide');
                $('#addAccountForm')[0].reset();
                location.reload();
            },
            error: function(xhr, status, error) {
                console.error('AJAX error:', status, error, xhr.responseText);
                var errorMessage = xhr.responseText || 'Đã xảy ra lỗi khi thêm tài khoản';
                alert(errorMessage);
            }
        });
    });

    // Xử lý sự kiện click cho nút Lưu thêm
    $('#saveNewAccountBtn').on('click', function() {
        console.log('Save button #saveNewAccountBtn clicked');
        $('#addAccountForm').trigger('submit');
    });

    // Xử lý sự kiện xóa tài khoản
    $('.btn-danger').on('click', function() {
        console.log('Delete button clicked');
        var accountId = $(this).data('account-id');
        if (confirm('Bạn có chắc muốn xóa tài khoản này?')) {
            $.ajax({
                url: '/admin/account/delete/' + accountId,
                type: 'DELETE',
                success: function(response) {
                    console.log('Account deleted successfully:', response);
                    alert('Xóa tài khoản thành công');
                    location.reload();
                },
                error: function(xhr, status, error) {
                    console.error('AJAX error:', status, error, xhr.responseText);
                    var errorMessage = xhr.responseText || 'Đã xảy ra lỗi khi xóa tài khoản';
                    alert(errorMessage);
                }
            });
        }
    });

    // Xử lý sự kiện mở modal sửa tài khoản
    $('.btn-info').on('click', function() {
        console.log('Edit button clicked');
        var accountId = $(this).data('account-id');
        $.ajax({
            url: '/admin/account/' + accountId,
            type: 'GET',
            success: function(account) {
                console.log('Fetched account data:', account);
                $('#edit_id').val(account.id);
                $('#edit_username').val(account.username);
                $('#edit_password').val(''); // Để trống để người dùng nhập mật khẩu mới
                $('#edit_role').val(account.role);
            },
            // error: function(xhr, status, error) {
            //     console.error('AJAX error fetching account:', status, error, xhr.responseText);
            //     alert('Đã xảy ra lỗi khi lấy dữ liệu tài khoản');
            // }
        });
    });

    // Xử lý sự kiện submit form sửa tài khoản
    $('#editAccountForm').on('submit', function(event) {
        event.preventDefault();
        console.log('Form #editAccountForm submitted');
        var id = $('#edit_id').val();
        var username = $('#edit_username').val().trim();
        var password = $('#edit_password').val().trim();
        var role = $('#edit_role').val();
        console.log('Input data for update:', { id: id, username: username, password: password, role: role });

        if (!password) {
            alert('Vui lòng nhập mật khẩu');
            return;
        }
        if (password.length < 6) {
            alert('Mật khẩu phải có ít nhất 6 ký tự');
            return;
        }
        if (!role) {
            alert('Vui lòng chọn quyền');
            return;
        }

        var account = {
            id: id,
            username: username,
            password: password,
            role: role
        };
        console.log('Sending AJAX update request with data:', account);
        $.ajax({
            url: '/admin/account/update',
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(account),
            success: function(response) {
                console.log('AJAX update success:', response);
                alert('Cập nhật tài khoản thành công');
                $('#editAccountModal').modal('hide');
                location.reload();
            },
            error: function(xhr, status, error) {
                console.error('AJAX update error:', status, error, xhr.responseText);
                var errorMessage = xhr.responseText || 'Đã xảy ra lỗi khi cập nhật tài khoản';
                alert(errorMessage);
            }
        });
    });

    // Xử lý sự kiện click cho nút Lưu thay đổi
    $('#saveUpdateAccountBtn').on('click', function() {
        console.log('Update button #saveUpdateAccountBtn clicked');
        $('#editAccountForm').trigger('submit');
    });
});