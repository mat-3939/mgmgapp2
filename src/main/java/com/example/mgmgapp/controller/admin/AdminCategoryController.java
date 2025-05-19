package com.example.mgmgapp.controller.admin;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.mgmgapp.entity.Categories;
import com.example.mgmgapp.form.CategoryForm;
import com.example.mgmgapp.service.admin.AdminCategoryService;

/**
 * 管理者用のカテゴリ操作コントローラ
 * カテゴリの一覧表示、登録、更新、削除などを処理する
 */
@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @Autowired
    public AdminCategoryController(AdminCategoryService adminCategoryService) {
        this.adminCategoryService = adminCategoryService;
    }

    /**
     * カテゴリ一覧表示
     */
    @GetMapping
    public String listCategories(Model model) {
        List<Categories> categories = adminCategoryService.findAll();
        model.addAttribute("categories", categories);
        return "admin/categories";
    }

    /**
     * カテゴリ新規登録フォーム表示
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "admin/category_form";
    }

    /**
     * カテゴリ新規登録
     */
    @PostMapping("/new")
    public String createCategory(@Valid @ModelAttribute CategoryForm categoryForm,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            return "admin/category_form";
        }

        Categories category = new Categories();
        category.setName(categoryForm.getName());
        adminCategoryService.createCategory(category);
        return "redirect:/admin/categories";
    }

    /**
     * カテゴリ編集フォーム表示
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Categories category = adminCategoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        CategoryForm form = new CategoryForm();
        form.setName(category.getName());
        model.addAttribute("categoryForm", form);
        return "admin/category_form";
    }

    /**
     * カテゴリ更新
     */
    @PostMapping("/{id}/edit")
    public String updateCategory(@PathVariable Integer id,
                                 @Valid @ModelAttribute CategoryForm categoryForm,
                                 BindingResult result) {
        if (result.hasErrors()) {
            return "admin/category_form";
        }

        Categories category = adminCategoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        category.setName(categoryForm.getName());
        adminCategoryService.updateCategory(category);
        return "redirect:/admin/categories";
    }

    /**
     * カテゴリ削除
     */
    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Integer id) {
        adminCategoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }
}
