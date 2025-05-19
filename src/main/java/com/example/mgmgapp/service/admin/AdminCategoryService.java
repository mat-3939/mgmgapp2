package com.example.mgmgapp.service.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mgmgapp.entity.Categories;
import com.example.mgmgapp.repository.admin.AdminCategoryRepository;

@Service
public class AdminCategoryService {

    private final AdminCategoryRepository adminCategoryRepository;

    @Autowired
    public AdminCategoryService(AdminCategoryRepository adminCategoryRepository) {
        this.adminCategoryRepository = adminCategoryRepository;
    }

    /**
     * 全カテゴリを取得
     */
    public List<Categories> findAll() {
        return adminCategoryRepository.findAll();
    }

    /**
     * IDからカテゴリを取得
     */
    public Optional<Categories> getCategoryById(Integer id) {
        return adminCategoryRepository.findById(id);
    }

    /**
     * カテゴリを新規作成
     */
    public Categories createCategory(Categories category) {
        return adminCategoryRepository.save(category);
    }

    /**
     * カテゴリを更新
     */
    public Categories updateCategory(Categories category) {
        return adminCategoryRepository.save(category);
    }

    /**
     * カテゴリを削除
     */
    public void deleteCategory(Integer id) {
    	adminCategoryRepository.deleteById(id);
    }
}
