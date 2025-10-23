package com.example.fruitstore.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chitietdonhang")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // lưu sanPhamId (FK) chứ không lưu tenSanPham ở đây
    @Column(name = "sanPhamId")
    private Long sanPhamId;

    @Column(name = "soLuong")
    private Integer soLuong;

    @Column(name = "gia")
    private Double gia;

    // nếu bạn muốn join tới OrderEntity, đặt JoinColumn trùng tên cột trong DB
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donHangId") // <-- đúng với cột trong DB (donHangId)
    private OrderEntity order;
}
