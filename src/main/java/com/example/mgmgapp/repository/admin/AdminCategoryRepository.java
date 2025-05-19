package com.example.mgmgapp.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mgmgapp.entity.Categories;

/**
 * 管理者用カテゴリリポジトリ
 */
public interface AdminCategoryRepository extends JpaRepository<Categories, Integer> {
	
	/**
     * すべてのカテゴリを取得するメソッド
     */
	List<Categories> findAll();

    /**
     * カテゴリ名からカテゴリを検索
     */
    Optional<Categories> findByName(String name);

    /**
     * 並び順でソートされたカテゴリ一覧を取得
     */
//    List<Categories> findAllByOrderBySortOrderAsc();
}
