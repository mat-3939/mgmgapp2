package com.example.mgmgapp.service.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mgmgapp.entity.Categories;
import com.example.mgmgapp.repository.user.CategoryRepository;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;
	
	@Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
	
	/**
     * 全カテゴリを取得
     */
    public List<Categories> findAll() {
        return categoryRepository.findAll();
    }
    
    /**
     * IDからカテゴリを取得
     */
    public Optional<Categories> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }
    
    
}
