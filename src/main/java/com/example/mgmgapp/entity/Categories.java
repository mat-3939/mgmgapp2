package com.example.mgmgapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

/**
 * 商品カテゴリ情報を管理するエンティティクラス
 */
@Data
@Entity
@Table(name = "categories")
public class Categories {

    /**
     * カテゴリID（主キー）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * カテゴリ名（not null、最大30文字、一意）
     */
    @Column(name = "name", nullable = false, length = 30, unique = true)
    private String name;
}
