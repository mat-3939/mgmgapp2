package com.example.mgmgapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

/**
 * 注文商品情報を管理するエンティティクラス
 */
@Data
@Entity
@Table(name = "order_items")
public class OrderItems {

    /**
     * 注文商品ID（主キー）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    /**
     * 注文ID（not null、多対一の関連）
     */
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    /**
     * 商品名（not null）
     */
    @Column(name = "product_name", nullable = false)
    private String productName;

    /**
     * 注文数量（not null）
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * 注文時の商品単価（not null、小数点以下2桁まで）
     */
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private int price;
}