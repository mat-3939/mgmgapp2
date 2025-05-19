package com.example.mgmgapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "admins")
public class Admins {

    /**
     * 管理者ID（主キー）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    /**
     * 管理者名（not null）
     */
    @Column(name = "user_name", nullable = false)
    private String userName;

    /**
     * 管理者パスワード（not null）
     */
    @Column(name = "password", nullable = false)
    private String password;
}
