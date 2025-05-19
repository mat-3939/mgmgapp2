package com.example.mgmgapp.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mgmgapp.entity.Categories;

public interface CategoryRepository extends JpaRepository<Categories, Integer>{
	
	/**
     * すべてのカテゴリを取得するメソッド
     */
	List<Categories> findAll();
	
	/**
     * カテゴリ名からカテゴリを検索
     */
    Optional<Categories> findByName(String name);

}
