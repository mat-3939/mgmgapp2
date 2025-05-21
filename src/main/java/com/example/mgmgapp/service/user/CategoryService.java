package com.example.mgmgapp.service.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.mgmgapp.entity.Categories;
import com.example.mgmgapp.repository.user.CategoryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	
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
