-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 11, 2025 at 04:01 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `fruitstore`
--

-- --------------------------------------------------------

--
-- Table structure for table `baiviet`
--

CREATE TABLE `baiviet` (
  `id` int(11) NOT NULL,
  `maBaiViet` varchar(50) NOT NULL,
  `tieuDe` varchar(200) NOT NULL,
  `noiDung` text DEFAULT NULL,
  `ngayDang` date NOT NULL,
  `nhanVienId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `baiviet`
--

INSERT INTO `baiviet` (`id`, `maBaiViet`, `tieuDe`, `noiDung`, `ngayDang`, `nhanVienId`) VALUES
(1, 'BV01', 'Meo bao quan trai cay', 'Nen de trong tu lanh o 5 do C', '2025-09-01', 1),
(2, 'BV02', 'Loi ich an nho', 'Nho giau vitamin C', '2025-09-05', 2);

-- --------------------------------------------------------

--
-- Table structure for table `baocao`
--

CREATE TABLE `baocao` (
  `MaBaoCao` int(11) NOT NULL,
  `NgayTao` date NOT NULL,
  `LoaiBaoCao` varchar(50) NOT NULL,
  `TongDoanhThu` decimal(15,2) DEFAULT 0.00,
  `TongDonHang` int(11) DEFAULT 0,
  `TongSanPhamTon` int(11) DEFAULT 0,
  `NguoiLap` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `baocao`
--

INSERT INTO `baocao` (`MaBaoCao`, `NgayTao`, `LoaiBaoCao`, `TongDoanhThu`, `TongDonHang`, `TongSanPhamTon`, `NguoiLap`) VALUES
(1, '2025-09-10', 'Doanh thu thang', 2000000.00, 10, 500, 'Nguyen Van A'),
(2, '2025-09-15', 'Ton kho', 0.00, 0, 450, 'Tran Thi B');

-- --------------------------------------------------------

--
-- Table structure for table `binhluan`
--

CREATE TABLE `binhluan` (
  `id` int(11) NOT NULL,
  `maBL` varchar(20) DEFAULT NULL,
  `noiDung` text NOT NULL,
  `ngayBL` timestamp NOT NULL DEFAULT current_timestamp(),
  `danhGia` int(11) DEFAULT NULL CHECK (`danhGia` >= 1 and `danhGia` <= 5),
  `sanPhamId` int(11) NOT NULL,
  `khachHangId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `binhluan`
--

INSERT INTO `binhluan` (`id`, `maBL`, `noiDung`, `ngayBL`, `danhGia`, `sanPhamId`, `khachHangId`) VALUES
(1, 'BL01', 'San pham rat tot', '2025-09-18 06:31:03', 5, 1, 1),
(2, 'BL02', 'Gia hop ly', '2025-09-18 06:31:03', 4, 2, 2),
(3, 'BL03', 'Nho ngot', '2025-09-18 06:31:03', 5, 3, 3);

-- --------------------------------------------------------

--
-- Table structure for table `chitietdonhang`
--

CREATE TABLE `chitietdonhang` (
  `id` int(11) NOT NULL,
  `donHangId` int(11) NOT NULL,
  `sanPhamId` int(11) NOT NULL,
  `soLuong` int(11) NOT NULL,
  `gia` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `chitietdonhang`
--

INSERT INTO `chitietdonhang` (`id`, `donHangId`, `sanPhamId`, `soLuong`, `gia`) VALUES
(1, 1, 1, 2, 60000.00),
(2, 2, 2, 3, 60000.00),
(3, 3, 3, 2, 80000.00);

-- --------------------------------------------------------

--
-- Table structure for table `chitietgiohang`
--

CREATE TABLE `chitietgiohang` (
  `id` int(11) NOT NULL,
  `gioHangId` int(11) NOT NULL,
  `sanPhamId` int(11) NOT NULL,
  `soLuong` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `chitietgiohang`
--

INSERT INTO `chitietgiohang` (`id`, `gioHangId`, `sanPhamId`, `soLuong`) VALUES
(1, 1, 1, 10),
(5, 1, 2, 3),
(7, 1, 7, 1);

-- --------------------------------------------------------

--
-- Table structure for table `danhmuc`
--

CREATE TABLE `danhmuc` (
  `id` int(11) NOT NULL,
  `maDanhMuc` varchar(20) NOT NULL,
  `tenDanhMuc` varchar(100) NOT NULL,
  `moTa` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `danhmuc`
--

INSERT INTO `danhmuc` (`id`, `maDanhMuc`, `tenDanhMuc`, `moTa`) VALUES
(1, 'DM01', 'Trai cay noi dia', 'Trai cay Viet Nam'),
(3, 'DM02', 'trái cây nhập khẩu', 'nhập từ nước ngoài ngoài về ');

-- --------------------------------------------------------

--
-- Table structure for table `donhang`
--

CREATE TABLE `donhang` (
  `id` int(11) NOT NULL,
  `maDonHang` varchar(50) NOT NULL,
  `tenNguoiNhan` varchar(100) NOT NULL,
  `soDienThoaiNguoiNhan` varchar(15) NOT NULL,
  `emailNguoiNhan` varchar(100) DEFAULT NULL,
  `ngayTao` date NOT NULL,
  `trangThai` enum('ChoXuLy','XacNhan','DangGiao','HoanThanh','DaHuy') NOT NULL DEFAULT 'ChoXuLy',
  `ghiChu` text DEFAULT NULL,
  `diaChiGiaoHang` varchar(255) NOT NULL,
  `tongTien` decimal(12,2) NOT NULL,
  `khachHangId` int(11) NOT NULL,
  `phuongThucThanhToanId` int(11) DEFAULT NULL,
  `khuyenMaiId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `donhang`
--

INSERT INTO `donhang` (`id`, `maDonHang`, `tenNguoiNhan`, `soDienThoaiNguoiNhan`, `emailNguoiNhan`, `ngayTao`, `trangThai`, `ghiChu`, `diaChiGiaoHang`, `tongTien`, `khachHangId`, `phuongThucThanhToanId`, `khuyenMaiId`) VALUES
(1, 'DH01', '', '', NULL, '2025-09-01', 'ChoXuLy', 'Giao gap', 'xóm 9 thôn nghĩa phương xã đông hoà thành phố thái bình', 50000.00, 1, 1, 2),
(2, 'DH02', '', '', NULL, '2025-09-02', 'DangGiao', '', 'văn quán ', 80000.00, 2, 2, NULL),
(3, 'DH03', '', '', NULL, '2025-09-02', 'DaHuy', 'Giao thanh cong', 'triều khúc', 60000.00, 3, 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `giohang`
--

CREATE TABLE `giohang` (
  `id` int(11) NOT NULL,
  `khachHangId` int(11) NOT NULL,
  `ngayTao` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `giohang`
--

INSERT INTO `giohang` (`id`, `khachHangId`, `ngayTao`) VALUES
(1, 1, '2025-09-18 06:31:03'),
(2, 2, '2025-09-18 06:31:03');

-- --------------------------------------------------------

--
-- Table structure for table `khachhang`
--

CREATE TABLE `khachhang` (
  `id` int(11) NOT NULL,
  `maKhachHang` varchar(20) NOT NULL,
  `tenKhachHang` varchar(100) NOT NULL,
  `diaChi` text DEFAULT NULL,
  `soDienThoai` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `loaiKhachHang` enum('moi','thuong','vip','tam_thoi') DEFAULT 'moi',
  `taiKhoanId` int(11) DEFAULT NULL,
  `trangThaiTaiKhoan` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0 = Ngưng hoạt động, 1 = Đang hoạt động'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `khachhang`
--

INSERT INTO `khachhang` (`id`, `maKhachHang`, `tenKhachHang`, `diaChi`, `soDienThoai`, `email`, `loaiKhachHang`, `taiKhoanId`, `trangThaiTaiKhoan`) VALUES
(1, 'KH001', 'Nguyen Van M', 'Ha Noi', '0981111111', 'm@gmail.com', 'vip', 1, 1),
(2, 'KH002', 'Tran Thi N', 'HCM', '0982222222', 'n@gmail.com', 'thuong', 2, 1),
(3, 'KH003', 'Le Van O', 'Da Nang', '0983333333', 'o@gmail.com', 'moi', 3, 1),
(4, 'KH004', 'Pham Thi P', 'Hai Phong', '0984444444', 'p@gmail.com', 'moi', 4, 1),
(5, 'KH005', 'Hoang Van Q', 'Hue', '0985555555', 'q@gmail.com', 'vip', 5, 1),
(6, 'KH006', 'Nguyen Thi R', 'Can Tho', '0986666666', 'r@gmail.com', 'thuong', 6, 1),
(7, 'KH007', 'Tran Van S', 'Quang Ninh', '0987777777', 's@gmail.com', 'tam_thoi', 7, 1),
(8, 'KH008', 'Do Thi T', 'Ninh Binh', '0988888888', 't@gmail.com', 'vip', 8, 1),
(9, 'KH009', 'Nguyen Van U', 'Thanh Hoa', '0989999999', 'u@gmail.com', 'thuong', 9, 1),
(10, 'KH010', 'Pham Thi V', 'Nam Dinh', '0970000000', 'v@gmail.com', 'moi', 10, 1);

-- --------------------------------------------------------

--
-- Table structure for table `khuyenmai`
--

CREATE TABLE `khuyenmai` (
  `id` int(11) NOT NULL,
  `maKM` varchar(20) NOT NULL,
  `tenKM` varchar(100) NOT NULL,
  `giaTri` decimal(10,2) NOT NULL,
  `ngayBatDau` date NOT NULL,
  `ngayKetThuc` date NOT NULL,
  `moTa` text DEFAULT NULL,
  `trangThai` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0 = Het hieu luc, 1 = Dang ap dung',
  `giaTriDonHangToiThieu` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT 'Giá trị đơn hàng tối thiểu để áp dụng khuyến mãi. Mặc định là 0 (không có điều kiện).'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `khuyenmai`
--

INSERT INTO `khuyenmai` (`id`, `maKM`, `tenKM`, `giaTri`, `ngayBatDau`, `ngayKetThuc`, `moTa`, `trangThai`, `giaTriDonHangToiThieu`) VALUES
(1, 'KM01', 'Sale Tet', 20000.00, '2025-01-01', '2025-02-01', 'Giam gia Tet', 1, 50000.00),
(2, 'KM02', 'Sale He', 10000.00, '2025-06-01', '2025-07-01', 'Giam gia mua he', 1, 20000.00);

-- --------------------------------------------------------

--
-- Table structure for table `nhacungcap`
--

CREATE TABLE `nhacungcap` (
  `nhaCungCapId` int(11) NOT NULL,
  `maNCC` varchar(20) NOT NULL,
  `tenNCC` varchar(100) NOT NULL,
  `soDienThoai` varchar(15) DEFAULT NULL,
  `diaChi` text DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `nhacungcap`
--

INSERT INTO `nhacungcap` (`nhaCungCapId`, `maNCC`, `tenNCC`, `soDienThoai`, `diaChi`, `email`) VALUES
(1, 'NCC01', 'CTY Rau Qua VN', '0911111111', 'Ha Noi', 'rauqua@gmail.com'),
(3, 'BK004', 'Bách Khoa fruit', '0348830868', 'thái bình', 'bachahuu182004@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `nhanvien`
--

CREATE TABLE `nhanvien` (
  `id` int(11) NOT NULL,
  `maNhanVien` varchar(20) NOT NULL,
  `tenNhanVien` varchar(100) NOT NULL,
  `soDienThoai` varchar(15) DEFAULT NULL,
  `diaChi` text DEFAULT NULL,
  `chucVu` varchar(50) DEFAULT NULL,
  `ngayVaoLam` timestamp NULL DEFAULT current_timestamp(),
  `taiKhoanId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `nhanvien`
--

INSERT INTO `nhanvien` (`id`, `maNhanVien`, `tenNhanVien`, `soDienThoai`, `diaChi`, `chucVu`, `ngayVaoLam`, `taiKhoanId`) VALUES
(1, 'NV001', 'Nguyen Van A', '0348830862', 'Ha Noi', 'Quan ly', '2025-09-18 06:31:03', 1),
(2, 'NV002', 'Tran Thi B', '0902222222', 'HCM', 'Nhan vien', '2025-09-18 06:31:03', 2),
(3, 'NV003', 'Le Van C', '0903333333', 'Da Nang', 'Nhan vien', '2025-09-18 06:31:03', 3),
(4, 'NV004', 'Pham Thi D', '0904444444', 'Hai Phong', 'Nhan vien', '2025-09-18 06:31:03', 4),
(5, 'NV005', 'Nguyen Van E', '0905555555', 'Can Tho', 'Nhan vien', '2025-09-18 06:31:03', 5),
(6, 'NV006', 'Hoang Thi F', '0906666666', 'Hue', 'Nhan vien', '2025-09-18 06:31:03', 6),
(7, 'NV007', 'Tran Van G', '0907777777', 'Quang Ninh', 'Nhan vien', '2025-09-18 06:31:03', 7),
(8, 'NV008', 'Do Thi H', '0908888888', 'Ninh Binh', 'Nhan vien', '2025-09-18 06:31:03', 8),
(9, 'NV009', 'Nguyen Van I', '0909999999', 'Thanh Hoa', 'Nhan vien', '2025-09-18 06:31:03', 9);

-- --------------------------------------------------------

--
-- Table structure for table `sanpham`
--

CREATE TABLE `sanpham` (
  `id` int(11) NOT NULL,
  `maSanPham` varchar(50) NOT NULL,
  `tenSanPham` varchar(200) NOT NULL,
  `gia` decimal(10,2) NOT NULL,
  `moTa` text DEFAULT NULL,
  `hinhAnh` varchar(255) DEFAULT NULL,
  `soLuongTon` int(11) DEFAULT 0,
  `danhMucId` int(11) DEFAULT NULL,
  `nhaCungCapId` int(11) DEFAULT NULL,
  `khuyenMaiId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `sanpham`
--

INSERT INTO `sanpham` (`id`, `maSanPham`, `tenSanPham`, `gia`, `moTa`, `hinhAnh`, `soLuongTon`, `danhMucId`, `nhaCungCapId`, `khuyenMaiId`) VALUES
(1, 'SP01', 'Tao My', 30000.00, 'Tao nhap khau tu My', 'tao.jpg', 100, NULL, NULL, 1),
(2, 'SP02', 'Cam Sai Gon', 20000.00, 'Cam tuoi ngon', 'cam.jpg', 200, 1, 1, NULL),
(3, 'SP03', 'Nho Uc', 40000.00, 'Nho do tu Uc', 'nho.jpg', 150, NULL, NULL, 2),
(4, 'SP04', 'Chuoi Tieu', 15000.00, 'Chuoi tuoi ngon', 'chuoi.jpg', 180, 1, 1, 1),
(5, 'SP05', 'Xoai Cat', 35000.00, 'Xoai cat hoa loc', 'xoai.jpg', 120, 1, 3, NULL),
(6, 'SP06', 'Dua Hau', 25000.00, 'Dua hau ruot do', 'duahau.jpg', 90, 1, 1, 2),
(7, 'SP07', 'Le Han Quoc', 45000.00, 'Le ngon nhap khau Han Quoc', 'le.jpg', 75, 3, 3, NULL),
(8, 'SP08', 'Mit Thai', 30000.00, 'Mit ngot mem', 'mit.jpg', 110, 3, 1, NULL),
(9, 'SP09', 'Thanh Long', 20000.00, 'Thanh long ruot do', 'thanhlong.jpg', 200, 1, 1, 1),
(10, 'SP10', 'Dua Luoi', 28000.00, 'Dua luoi ngot mat', 'dualuoi.jpg', 150, 3, 3, NULL),
(11, 'SP11', 'Man Hau', 40000.00, 'Man hau tuoi', 'man.jpg', 100, 1, 1, 2),
(12, 'SP12', 'Dau Tay Da Lat', 60000.00, 'Dau tay tuoi Da Lat', 'dautay.jpg', 80, 1, 3, NULL),
(13, 'SP13', 'Oi Xanh', 18000.00, 'Oi xanh tuoi', 'oi.jpg', 170, 1, 1, NULL),
(14, 'SP14', 'Sau Rieng Ri6', 70000.00, 'Sau rieng Ri6 chinh goc', 'saurieng.jpg', 60, 3, 3, NULL),
(15, 'SP15', 'Mang Cau', 35000.00, 'Mang cau ta', 'mangcau.jpg', 90, 1, 1, 1),
(16, 'SP16', 'Coc Xanh', 25000.00, 'Coc xanh chua ngot', 'coc.jpg', 140, 1, 3, NULL),
(17, 'SP17', 'Hong Do', 30000.00, 'Hong do tuoi', 'hong.jpg', 85, 1, 1, NULL),
(18, 'SP18', 'Buoi Nam Roi', 40000.00, 'Buoi Nam Roi chinh goc', 'buoi.jpg', 120, 1, 3, NULL),
(19, 'SP19', 'Chom Chom', 28000.00, 'Chom chom tuoi ngon', 'chomchom.jpg', 200, 1, 1, 2),
(20, 'SP20', 'Me Thai', 35000.00, 'Me chua Thai Lan', 'me.jpg', 150, 3, 3, NULL),
(21, 'SP21', 'Na Dai', 30000.00, 'Na dai ngot', 'na.jpg', 100, 1, 1, NULL),
(22, 'SP22', 'Tao Xanh', 32000.00, 'Tao xanh gion', 'taoxanh.jpg', 110, 3, 3, 1),
(23, 'SP23', 'Man Do', 38000.00, 'Man do tuoi', 'mando.jpg', 95, 1, 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `taikhoancuahang`
--

CREATE TABLE `taikhoancuahang` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('Admin','NV') DEFAULT 'NV'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `taikhoancuahang`
--

INSERT INTO `taikhoancuahang` (`id`, `username`, `password`, `role`) VALUES
(1, 'admin1', '123456', 'Admin'),
(2, 'nv1', '123456', 'NV'),
(3, 'nv2', '123456', 'NV'),
(4, 'nv3', '123456', 'NV'),
(5, 'nv4', '123456', 'NV'),
(6, 'nv5', '123456', 'NV'),
(7, 'nv6', '123456', 'NV'),
(8, 'nv7', '123456', 'NV'),
(9, 'nv8', '123456', 'NV'),
(10, 'nv9', '123456', 'NV');

-- --------------------------------------------------------

--
-- Table structure for table `taikhoankhachhang`
--

CREATE TABLE `taikhoankhachhang` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `taikhoankhachhang`
--

INSERT INTO `taikhoankhachhang` (`id`, `username`, `password`) VALUES
(1, 'kh1', '123456'),
(2, 'kh2', '123456'),
(3, 'kh3', '123456'),
(4, 'kh4', '123456'),
(5, 'kh5', '123456'),
(6, 'kh6', '123456'),
(7, 'kh7', '123456'),
(8, 'kh8', '123456'),
(9, 'kh9', '123456'),
(10, 'kh10', '123456');

-- --------------------------------------------------------

--
-- Table structure for table `thanhtoan`
--

CREATE TABLE `thanhtoan` (
  `id` int(11) NOT NULL,
  `tenPTTT` varchar(100) NOT NULL,
  `moTa` text DEFAULT NULL,
  `trangThai` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0 = Ngưng sử dụng, 1 = Đang hoạt động'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `thanhtoan`
--

INSERT INTO `thanhtoan` (`id`, `tenPTTT`, `moTa`, `trangThai`) VALUES
(1, 'Tien mat', 'Thanh toan khi nhan hang', 1),
(2, 'Chuyen khoan', 'Thanh toan qua ngan hang', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `baiviet`
--
ALTER TABLE `baiviet`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `maBaiViet` (`maBaiViet`),
  ADD KEY `baiviet_ibfk_1` (`nhanVienId`);

--
-- Indexes for table `baocao`
--
ALTER TABLE `baocao`
  ADD PRIMARY KEY (`MaBaoCao`);

--
-- Indexes for table `binhluan`
--
ALTER TABLE `binhluan`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `maBL` (`maBL`),
  ADD KEY `binhluan_ibfk_1` (`sanPhamId`),
  ADD KEY `binhluan_ibfk_2` (`khachHangId`);

--
-- Indexes for table `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  ADD PRIMARY KEY (`id`),
  ADD KEY `chitietdonhang_ibfk_1` (`donHangId`),
  ADD KEY `chitietdonhang_ibfk_2` (`sanPhamId`);

--
-- Indexes for table `chitietgiohang`
--
ALTER TABLE `chitietgiohang`
  ADD PRIMARY KEY (`id`),
  ADD KEY `chitietgiohang_ibfk_1` (`gioHangId`),
  ADD KEY `chitietgiohang_ibfk_2` (`sanPhamId`);

--
-- Indexes for table `danhmuc`
--
ALTER TABLE `danhmuc`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `maDanhMuc` (`maDanhMuc`);

--
-- Indexes for table `donhang`
--
ALTER TABLE `donhang`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `maDonHang` (`maDonHang`),
  ADD KEY `donhang_ibfk_1` (`khachHangId`),
  ADD KEY `donhang_ibfk_2` (`phuongThucThanhToanId`),
  ADD KEY `fk_donhang_khuyenmai` (`khuyenMaiId`);

--
-- Indexes for table `giohang`
--
ALTER TABLE `giohang`
  ADD PRIMARY KEY (`id`),
  ADD KEY `giohang_ibfk_1` (`khachHangId`);

--
-- Indexes for table `khachhang`
--
ALTER TABLE `khachhang`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `maKhachHang` (`maKhachHang`),
  ADD KEY `khachhang_ibfk_1` (`taiKhoanId`);

--
-- Indexes for table `khuyenmai`
--
ALTER TABLE `khuyenmai`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `maKM` (`maKM`);

--
-- Indexes for table `nhacungcap`
--
ALTER TABLE `nhacungcap`
  ADD PRIMARY KEY (`nhaCungCapId`),
  ADD UNIQUE KEY `maNCC` (`maNCC`);

--
-- Indexes for table `nhanvien`
--
ALTER TABLE `nhanvien`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `maNhanVien` (`maNhanVien`),
  ADD KEY `nhanvien_ibfk_1` (`taiKhoanId`);

--
-- Indexes for table `sanpham`
--
ALTER TABLE `sanpham`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `maSanPham` (`maSanPham`),
  ADD KEY `fk_sanpham_km` (`khuyenMaiId`),
  ADD KEY `fk_sanpham_ncc` (`nhaCungCapId`),
  ADD KEY `fk_sanpham_dm` (`danhMucId`);

--
-- Indexes for table `taikhoancuahang`
--
ALTER TABLE `taikhoancuahang`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `taikhoankhachhang`
--
ALTER TABLE `taikhoankhachhang`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `thanhtoan`
--
ALTER TABLE `thanhtoan`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `baiviet`
--
ALTER TABLE `baiviet`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `baocao`
--
ALTER TABLE `baocao`
  MODIFY `MaBaoCao` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `binhluan`
--
ALTER TABLE `binhluan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `chitietgiohang`
--
ALTER TABLE `chitietgiohang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `danhmuc`
--
ALTER TABLE `danhmuc`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `donhang`
--
ALTER TABLE `donhang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `giohang`
--
ALTER TABLE `giohang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `khachhang`
--
ALTER TABLE `khachhang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `khuyenmai`
--
ALTER TABLE `khuyenmai`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `nhacungcap`
--
ALTER TABLE `nhacungcap`
  MODIFY `nhaCungCapId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `nhanvien`
--
ALTER TABLE `nhanvien`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `sanpham`
--
ALTER TABLE `sanpham`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `taikhoancuahang`
--
ALTER TABLE `taikhoancuahang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `taikhoankhachhang`
--
ALTER TABLE `taikhoankhachhang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `thanhtoan`
--
ALTER TABLE `thanhtoan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `baiviet`
--
ALTER TABLE `baiviet`
  ADD CONSTRAINT `baiviet_ibfk_1` FOREIGN KEY (`nhanVienId`) REFERENCES `nhanvien` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `binhluan`
--
ALTER TABLE `binhluan`
  ADD CONSTRAINT `binhluan_ibfk_1` FOREIGN KEY (`sanPhamId`) REFERENCES `sanpham` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `binhluan_ibfk_2` FOREIGN KEY (`khachHangId`) REFERENCES `khachhang` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  ADD CONSTRAINT `chitietdonhang_ibfk_1` FOREIGN KEY (`donHangId`) REFERENCES `donhang` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `chitietdonhang_ibfk_2` FOREIGN KEY (`sanPhamId`) REFERENCES `sanpham` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `chitietgiohang`
--
ALTER TABLE `chitietgiohang`
  ADD CONSTRAINT `chitietgiohang_ibfk_1` FOREIGN KEY (`gioHangId`) REFERENCES `giohang` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `chitietgiohang_ibfk_2` FOREIGN KEY (`sanPhamId`) REFERENCES `sanpham` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `donhang`
--
ALTER TABLE `donhang`
  ADD CONSTRAINT `donhang_ibfk_1` FOREIGN KEY (`khachHangId`) REFERENCES `khachhang` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `donhang_ibfk_2` FOREIGN KEY (`phuongThucThanhToanId`) REFERENCES `thanhtoan` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_donhang_khuyenmai` FOREIGN KEY (`khuyenMaiId`) REFERENCES `khuyenmai` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `giohang`
--
ALTER TABLE `giohang`
  ADD CONSTRAINT `giohang_ibfk_1` FOREIGN KEY (`khachHangId`) REFERENCES `khachhang` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `khachhang`
--
ALTER TABLE `khachhang`
  ADD CONSTRAINT `khachhang_ibfk_1` FOREIGN KEY (`taiKhoanId`) REFERENCES `taikhoankhachhang` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `nhanvien`
--
ALTER TABLE `nhanvien`
  ADD CONSTRAINT `nhanvien_ibfk_1` FOREIGN KEY (`taiKhoanId`) REFERENCES `taikhoancuahang` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `sanpham`
--
ALTER TABLE `sanpham`
  ADD CONSTRAINT `fk_sanpham_dm` FOREIGN KEY (`danhMucId`) REFERENCES `danhmuc` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sanpham_km` FOREIGN KEY (`khuyenMaiId`) REFERENCES `khuyenmai` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sanpham_ncc` FOREIGN KEY (`nhaCungCapId`) REFERENCES `nhacungcap` (`nhaCungCapId`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
