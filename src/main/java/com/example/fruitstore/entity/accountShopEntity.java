package com.example.fruitstore.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "taikhoancuahang")
@Data
public class accountShopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;
}
