package com.example.fruitstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.fruitstore.respository.SanPhamRepository;
import com.example.fruitstore.entity.DanhMuc;
import com.example.fruitstore.respository.DanhMucRepository;

@Service
public class DanhMucService {

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;
    /**
     * Lấy tất cả danh mục.
     */
    public List<DanhMuc> getAllDanhMuc() {
        return danhMucRepository.findAll();
    }

    /**
     * Lấy danh mục theo ID.
     * Cần thiết cho việc liên kết Danh Mục trong AdminProductController.
     */
    public DanhMuc getDanhMucById(Integer id) {
        return danhMucRepository.findById(id).orElse(null);
    }

    /**
     * Lấy danh mục theo ID (trả về Optional).
     */
    public Optional<DanhMuc> findById(Integer id) {
        return danhMucRepository.findById(id);
    }

    /**
     * Lưu (thêm mới hoặc cập nhật) danh mục.
     */
    public DanhMuc save(DanhMuc danhMuc) {
        return danhMucRepository.save(danhMuc);
    }

    /**
     * Xóa danh mục theo ID.
     */
    public void deleteById(Integer id) {
        danhMucRepository.deleteById(id);
    }

    // New methods: CRUD by maDanhMuc (category code)
    public DanhMuc getDanhMucByMaDanhMuc(String maDanhMuc) {
        return danhMucRepository.findByMaDanhMuc(maDanhMuc)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với mã: " + maDanhMuc));
    }

    public DanhMuc addDanhMuc(DanhMuc danhMuc) {
        if (danhMuc.getMaDanhMuc() != null && danhMucRepository.findByMaDanhMuc(danhMuc.getMaDanhMuc()).isPresent()) {
            throw new RuntimeException("Mã Danh Mục đã tồn tại: " + danhMuc.getMaDanhMuc());
        }
        return danhMucRepository.save(danhMuc);
    }

    public DanhMuc updateDanhMuc(String maDanhMuc, DanhMuc update) {
        DanhMuc dm = getDanhMucByMaDanhMuc(maDanhMuc);
        dm.setTenDanhMuc(update.getTenDanhMuc());
        dm.setMoTa(update.getMoTa());
        // Do not update maDanhMuc (code) here to keep identity stable; if needed, handle separately.
        return danhMucRepository.save(dm);
    }

    public void deleteByMaDanhMuc(String maDanhMuc) {
        // 1. Tìm danh mục (sẽ ném lỗi nếu không tìm thấy)
        DanhMuc dm = getDanhMucByMaDanhMuc(maDanhMuc);

        // 2. Kiểm tra xem có sản phẩm nào liên kết với danh mục này không
        boolean hasProducts = sanPhamRepository.existsByDanhMuc(dm);

        // 3. Nếu có, ném lỗi với thông báo tùy chỉnh
        if (hasProducts) {
            throw new RuntimeException("Không thể xóa danh mục đang có sản phẩm. Vui lòng gỡ sản phẩm khỏi danh mục này trước.");
        }

        // 4. Nếu không, tiến hành xóa
        danhMucRepository.delete(dm);
    }
}