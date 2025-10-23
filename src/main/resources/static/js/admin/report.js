// ===============================
//  ADMIN REPORT MANAGEMENT JS
// ===============================

// Hiển thị / ẩn input ngày - tháng - năm
function toggleInputs() {
  const mode = document.getElementById("modeSelect").value;
  ["inputDay", "inputMonthYear", "inputYear"].forEach(id =>
    document.getElementById(id).classList.add("d-none")
  );
  if (mode === "day") document.getElementById("inputDay").classList.remove("d-none");
  if (mode === "month") document.getElementById("inputMonthYear").classList.remove("d-none");
  if (mode === "year") document.getElementById("inputYear").classList.remove("d-none");
}

//  Tạo báo cáo
function createReport() {
  const type = document.getElementById("reportType").value;
  const creator = document.getElementById("reportCreator").value.trim();
  const note = document.getElementById("reportNote").value.trim();
  const revenue = parseFloat(document.getElementById("reportRevenue").value) || 0;
  const orders = parseInt(document.getElementById("reportOrders").value) || 0;
  const stock = parseInt(document.getElementById("reportStock").value) || 0;

  if (!creator) return alert("⚠️ Vui lòng nhập người lập báo cáo!");

  const data = {
    loaiBaoCao: type,
    nguoiLap: creator,
    ghiChu: note,
    tongDoanhThu: revenue,
    tongDonHang: orders,
    tongSanPhamTon: stock
  };

  fetch("/admin/report/create", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data)
  })
    .then(res => {
      if (!res.ok) throw new Error("Lỗi khi tạo báo cáo!");
      return res.json();
    })
    .then(() => {
      alert("✅ Báo cáo đã được tạo thành công!");
      resetReportForm();
      loadReports();
    })
    .catch(() => alert("❌ Lỗi khi tạo báo cáo!"));
}

//  Reset form nhập liệu
function resetReportForm() {
  ["reportCreator", "reportNote", "reportRevenue", "reportOrders", "reportStock"].forEach(id => {
    document.getElementById(id).value = "";
  });
  document.getElementById("reportType").selectedIndex = 0;
  document.getElementById("reportId").value = "";
  document.getElementById("createBtn").classList.remove("d-none");
  document.getElementById("updateBtn").classList.add("d-none");
  document.getElementById("cancelBtn").classList.add("d-none");
}

//  Tải danh sách báo cáo
function loadReports() {
  fetch("/admin/report/list")
    .then(res => res.json())
    .then(data => {
      if (!data.length) {
        document.getElementById("reportTable").innerHTML =
          `<div class="alert alert-info text-center">Chưa có báo cáo nào được tạo</div>`;
        return;
      }

      let html = `
        <table class="table table-bordered table-striped text-center align-middle">
          <thead class="table-primary">
            <tr>
              <th>Mã BC</th>
              <th>Loại báo cáo</th>
              <th>Người lập</th>
              <th>Ngày tạo</th>
              <th>Doanh thu (VNĐ)</th>
              <th>Đơn hàng</th>
              <th>Tồn kho</th>
              <th>Ghi chú</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
      `;

      data.forEach(r => {
        const reportData = encodeURIComponent(JSON.stringify(r));
        html += `
          <tr>
            <td>${r.id || r.maBaoCao}</td>
            <td>${r.loaiBaoCao}</td>
            <td>${r.nguoiLap}</td>
            <td>${r.ngayTao || r.createdAt || "-"}</td>
            <td>${Number(r.tongDoanhThu || 0).toLocaleString()}</td>
            <td>${r.tongDonHang || 0}</td>
            <td>${r.tongSanPhamTon || 0}</td>
            <td>${r.ghiChu || ""}</td>
            <td>
              <button class="btn btn-sm btn-warning me-1" onclick='selectReport(JSON.parse(decodeURIComponent("${reportData}")))'>✏️ Sửa</button>
              <button class="btn btn-sm btn-danger" onclick="deleteReport(${r.id || r.maBaoCao})">🗑️ Xoá</button>
            </td>
          </tr>`;
      });

      html += "</tbody></table>";
      document.getElementById("reportTable").innerHTML = html;
    })
    .catch(() => {
      document.getElementById("reportTable").innerHTML =
        `<div class="alert alert-danger text-center">⚠️ Lỗi khi tải danh sách báo cáo!</div>`;
    });
}

//  Cập nhật báo cáo
function updateReport() {
  const id = document.getElementById("reportId").value;
  if (!id) return alert("⚠️ Chưa chọn báo cáo để cập nhật!");

  const data = {
    loaiBaoCao: document.getElementById("reportType").value,
    nguoiLap: document.getElementById("reportCreator").value,
    ghiChu: document.getElementById("reportNote").value,
    tongDoanhThu: parseFloat(document.getElementById("reportRevenue").value) || 0,
    tongDonHang: parseInt(document.getElementById("reportOrders").value) || 0,
    tongSanPhamTon: parseInt(document.getElementById("reportStock").value) || 0
  };

  fetch(`/admin/report/update/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data)
  })
    .then(res => {
      if (!res.ok) throw new Error("Lỗi khi cập nhật!");
      return res.json();
    })
    .then(() => {
      alert("✅ Cập nhật báo cáo thành công!");
      resetReportForm();
      loadReports();
    })
    .catch(() => alert("❌ Lỗi khi cập nhật báo cáo!"));
}

//  Chọn báo cáo để sửa
function selectReport(report) {
  document.getElementById("reportType").value = report.loaiBaoCao;
  document.getElementById("reportCreator").value = report.nguoiLap;
  document.getElementById("reportNote").value = report.ghiChu || "";
  document.getElementById("reportRevenue").value = report.tongDoanhThu || 0;
  document.getElementById("reportOrders").value = report.tongDonHang || 0;
  document.getElementById("reportStock").value = report.tongSanPhamTon || 0;
  document.getElementById("reportId").value = report.id || report.maBaoCao;

  document.getElementById("createBtn").classList.add("d-none");
  document.getElementById("updateBtn").classList.remove("d-none");
  document.getElementById("cancelBtn").classList.remove("d-none");
}

//  Xoá báo cáo
function deleteReport(id) {
  if (!confirm("Bạn có chắc muốn xoá báo cáo này?")) return;
  fetch(`/admin/report/delete/${id}`, { method: "DELETE" })
    .then(res => {
      if (!res.ok) throw new Error("Lỗi xoá báo cáo!");
      alert("🗑️ Đã xoá báo cáo thành công!");
      loadReports();
    })
    .catch(() => alert("❌ Lỗi khi xoá báo cáo!"));
}

//  Huỷ chỉnh sửa
function cancelEdit() {
  resetReportForm();
}

function exportReportWord() {
  console.log("🚀 Bắt đầu xuất Word...");
  window.location.href = '/admin/report/export/word';
}


function getModeParams() {
  const mode = document.getElementById("modeSelect").value;
  if (mode === "day") return { mode, query: `?date=${document.getElementById("reportDate").value}` };
  if (mode === "month") {
    const y = document.getElementById("reportYear").value;
    const m = document.getElementById("reportMonth").value;
    return { mode, query: `?year=${y}&month=${m}` };
  }
  const y = document.getElementById("onlyYear").value;
  return { mode: "year", query: `?year=${y}` };
}

// 💰 Lấy doanh thu + sản phẩm bán được
function getRevenue() {
  const { mode, query } = getModeParams();
  const url = `/admin/report/revenue-with-products/${mode}${query}`;

  fetch(url)
    .then(res => {
      if (!res.ok) throw new Error("Không thể lấy dữ liệu doanh thu!");
      return res.json();
    })
    .then(data => {
      // Ép kiểu an toàn
      const totalRevenue = Number(data?.totalRevenue || 0);
      const products = Array.isArray(data?.products) ? data.products : [];

      // Khối hiển thị doanh thu tổng
      let html = `
        <div class="alert alert-success text-center fs-5">
          💰 Tổng doanh thu (${mode === "day" ? "Theo ngày" : mode === "month" ? "Theo tháng" : "Theo năm"}):
          <b>${totalRevenue.toLocaleString()} VNĐ</b>
        </div>
      `;

      // Nếu có danh sách sản phẩm bán được
      if (products.length > 0) {
        html += `
          <h5 class="text-center mt-4 fw-bold text-primary">
            📦 Danh sách sản phẩm bán được
          </h5>
          <table class="table table-bordered table-striped text-center mt-3">
            <thead class="table-warning">
              <tr>
                <th>Tên sản phẩm</th>
                <th>Số lượng bán</th>
                <th>Đơn giá (VNĐ)</th>
                <th>Thành tiền (VNĐ)</th>
              </tr>
            </thead>
            <tbody>
        `;

        // Lặp qua sản phẩm
        products.forEach(p => {
          const price = Number(p.donGia || 0);
          const quantity = Number(p.soLuong || 0);
          const subtotal = price * quantity;

          html += `
            <tr>
              <td>${p.tenSanPham}</td>
              <td>${quantity.toLocaleString()}</td>
              <td>${price.toLocaleString()}</td>
              <td>${subtotal.toLocaleString()}</td>
            </tr>
          `;
        });

        html += `
            </tbody>
          </table>
        `;
      } else {
        html += `
          <div class="alert alert-info text-center mt-3">
            Không có sản phẩm nào được bán trong khoảng thời gian này.
          </div>
        `;
      }

      document.getElementById("reportResult").innerHTML = html;
    })
    .catch(err => showError(err.message));
}

// 📦 Lấy số lượng đơn hàng theo chế độ (ngày/tháng/năm)
function getOrderCount() {
  const { mode, query } = getModeParams();
  const url = `/admin/report/orders/${mode}${query}`;

  fetch(url)
    .then(res => {
      if (!res.ok) throw new Error("Không thể lấy dữ liệu đơn hàng!");
      return res.json();
    })
    .then(data => {
      document.getElementById("reportResult").innerHTML = `
        <div class="alert alert-info text-center fs-5">
          📦 Số lượng đơn hàng (${mode === "day" ? "Theo ngày" : mode === "month" ? "Theo tháng" : "Theo năm"}): 
          <b>${data || 0}</b>
        </div>`;
    })
    .catch(err => showError(err.message));
}

// ⚠️ Hiển thị lỗi
function showError(msg) {
  document.getElementById("reportResult").innerHTML = `
    <div class="alert alert-danger text-center fs-5">
      ❌ ${msg}
    </div>`;
}

