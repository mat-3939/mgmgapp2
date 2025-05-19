package com.example.mgmgapp.repository.admin;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mgmgapp.entity.Products;

/**
 * 商品情報に関するリポジトリインタフェース。
 * Spring Data JPAを利用して、商品の検索・保存・削除などの操作を行う。
 * 通常のCRUD処理はJpaRepositoryで自動的に反映。
 */
@Repository
public interface AdminProductRepository extends JpaRepository<Products, Integer> {

    // --- 基本検索 ---

    /**
     * 商品名が完全一致する商品を検索
     */
    Optional<Products> findByName(String name);

    /**
     * 商品名に部分一致する商品を検索
     */
    List<Products> findByNameContaining(String name);

    /**
     * 指定したカテゴリの商品一覧を取得
     */
    List<Products> findByCategoryIdOrderByCreatedAtDesc(Integer categoryId);

    // --- 価格範囲検索 ---

    /**
     * 指定した価格帯の商品を取得（例：1000円以上、5000円以下）
     *
     * @param min 最低価格
     * @param max 最高価格
     */
    List<Products> findByPriceBetween(BigDecimal min, BigDecimal max);

    // --- ソート検索 ---

    /**
     * 登録日が新しい順（降順）
     */
    List<Products> findAllByOrderByCreatedAtDesc();

    /**
     * 登録日が古い順（昇順）
     */
    List<Products> findAllByOrderByCreatedAtAsc();
    
    /**
     * 更新日が新しい順（降順）
     */
    List<Products> findAllByOrderByUpdatedAtDesc();

    /**
     * 価格が高い順（降順）
     */
    List<Products> findAllByOrderByPriceDesc();

    /**
     * 価格が安い順（昇順）
     */
    List<Products> findAllByOrderByPriceAsc();

    /**
     * 名前順：五十音順（昇順）
     */
    List<Products> findAllByOrderByNameAsc();

    /**
     * 名前順：逆順（降順）
     */
    List<Products> findAllByOrderByNameDesc();
    
    /**
     * キーワード検索＆ソート
     */
    List<Products> findByNameContainingIgnoreCase(String name, Sort sort);
    
    /**
     * キーワード検索＆カテゴリ絞り込み＆ソート
     */
    List<Products> findByNameContainingAndCategoryId(String name, Integer categoryId, Sort sort);
    
    /**
     * カテゴリ指定＆ソート条件
     */
    List<Products> findByCategoryId(Integer categoryId, Sort sort);
}
