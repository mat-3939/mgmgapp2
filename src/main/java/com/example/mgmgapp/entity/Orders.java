package com.example.mgmgapp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注文情報を管理するエンティティクラス
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Orders {

    /**
     * 注文ID（主キー）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名（not null）
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * 姓（not null）
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * メールアドレス（not null）
     */
    @Column(name = "email", nullable = false)
    private String email;
    
    /**
     * 郵便番号（not null）
     */
    @Column(name = "postcode", nullable = false)
    private String postcode;

    /**
     * 都道府県（not null）
     */
    @Column(name = "prefecture", nullable = false)
    private String prefecture;

    /**
     * 市区町村（not null）
     */
    @Column(name = "city", nullable = false)
    private String city;

    /**
     * 住所（not null）
     */
    @Column(name = "address_line", nullable = false)
    private String addressLine;

    /**
     * 建物名（null）
     */
    @Column(name = "building", nullable = true)
    private String building;

    /**
     * 電話番号
     */
    @Column(name = "tel")
    private String tel;
    
    /**
     * 支払方法（クレジットカードのみ）
     */
    @Column(name = "pay_method", nullable = true)
    private String payMethod;

    /**
     * 合計金額（not null、小数点以下2桁まで）
     */
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private int totalPrice;
    
    /**
     * 注文作成日時
     */
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    /**
     * 注文ステータス
     */
    @Column(name = "status")
    private Boolean status;
    
    /**
     * 新規登録時の追加設定
     */
    @PrePersist
    public void onPrePersist() {
    	System.out.println("★★★ onPrePersist() called ★★★");
    	
//    	//注文ステータスがnull時に「未対応」を代入
//    	if (status == null) {
//		status = "未対応";
//        }
    	
    	//支払方法がnull時にクレジットカードを代入
    	if (payMethod == null) {
    		payMethod = "クレジットカード";
        }
    	
    	//注文時に注文日時を自動で設定
    	LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        this.orderDate = now;
    }
    
}

