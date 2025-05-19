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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.mgmgapp.entity.Categories;
import com.example.mgmgapp.entity.Products;
import com.example.mgmgapp.form.ProductForm;
import com.example.mgmgapp.service.admin.AdminCategoryService;
import com.example.mgmgapp.service.admin.AdminProductService;

/**
 * 管理者用の商品操作コントローラ
 * 商品の一覧表示、登録、更新、削除などを処理する
 */
@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

	private final AdminProductService adminProductService;
	private final AdminCategoryService adminCategoryService;

	@Autowired
	public AdminProductController(AdminProductService adminProductService,
	                              AdminCategoryService adminCategoryService) {
		this.adminProductService = adminProductService;
		this.adminCategoryService = adminCategoryService;
	}

	/**
	 * 商品一覧を表示（デフォルトは新着順）
	 */
	@GetMapping
	public String listProducts(
			@RequestParam(required = false) Integer categoryId,
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false, defaultValue = "new") String sort,
			Model model) {

		List<Products> products;

		// フィルタリング条件の分岐
    	if (keyword != null && !keyword.isEmpty()) {
            if (categoryId != null) {
                products = adminProductService.searchByKeywordAndCategorySorted(keyword, categoryId, sort);
            } else {
                products = adminProductService.searchByKeywordSorted(keyword, sort);
            }
        } else {
            if (categoryId != null) {
                products = adminProductService.findByCategoryIdSorted(categoryId, sort);
            } else {
                products = adminProductService.getSortedProducts(sort);
            }
        }

		model.addAttribute("products", products);
	    model.addAttribute("categories", adminCategoryService.findAll());
	    model.addAttribute("selectedCategoryId", categoryId);
	    model.addAttribute("sort", sort);
	    model.addAttribute("keyword", keyword);
		return "admin/products";
	}
	
	/**
	 * 指定したカテゴリの商品一覧を表示
	 */
	@GetMapping("/category/{categoryId}")
	public String listByCategory(@PathVariable Integer categoryId, Model model) {
		List<Products> products = adminProductService.findByCategoryId(categoryId);
		model.addAttribute("products", products);
		model.addAttribute("categories", adminCategoryService.findAll());
		model.addAttribute("selectedCategoryId", categoryId);
		return "admin/products";
	}

	/**
	 * 商品新規登録画面を表示
	 */
	@GetMapping("/new")
	public String showCreateForm(Model model) {
		model.addAttribute("productForm", new ProductForm());
		model.addAttribute("categories", adminCategoryService.findAll());
		return "admin/product_form";
	}

	/**
	 * 商品の新規登録を実行
	 */
	@PostMapping("/new")
	public String createProduct(@Valid @ModelAttribute ProductForm productForm,
	                            BindingResult result,
	                            /*
	                            @RequestParam("imageFile") MultipartFile imageFile,
	                            */
	                            Model model) {
		
		MultipartFile imageFile = productForm.getImageFile();
		
		if (result.hasErrors()) {
			model.addAttribute("categories", adminCategoryService.findAll());
			return "admin/product_form";
		}
		
		// 画像ファイルが存在し、拡張子が不正な場合
	    if (!imageFile.isEmpty() && !isAllowedExtension(imageFile.getOriginalFilename())) {
	        result.rejectValue("imageFile", "invalid.extension", "画像の拡張子は jpg、jpeg、png のいずれかにしてください。");
	        model.addAttribute("categories", adminCategoryService.findAll());
	        return "admin/product_form";
	    }

		// フォーム → エンティティ変換 & 画像保存
		Products product = convertToEntity(productForm, imageFile, null);

		adminProductService.createProduct(product);

		return "redirect:/admin/products";
	}

	/**
	 * 商品編集画面を表示
	 */
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Integer id, Model model) {
		Products product = adminProductService.getProductById(id).orElseThrow();
		ProductForm form = convertToForm(product);

		model.addAttribute("productForm", form);
		model.addAttribute("categories", adminCategoryService.findAll());
		model.addAttribute("formMode", "edit");

		return "admin/product_form";
	}

	/**
	 * 商品の更新を実行
	 */
	@PostMapping("/edit/{id}")
	public String updateProduct(@PathVariable Integer id,
	                            @Valid @ModelAttribute ProductForm productForm,
	                            BindingResult result,
	                            @RequestParam("imageFile") MultipartFile imageFile,
	                            Model model) {
		if (result.hasErrors()) {
			model.addAttribute("categories", adminCategoryService.findAll());
			return "admin/product_form";
		}

		// 既存の商品情報を取得
	    Products existingProduct = adminProductService.getProductById(id).orElseThrow();
	    
	    // 既存の商品データを元に新しいエンティティを作成
	    Products updatedProduct  = convertToEntity(productForm, imageFile, existingProduct);

	    // 商品情報の更新
	    adminProductService.updateProduct(updatedProduct);

		return "redirect:/admin/products";
	}

	/**
	 * 商品の削除
	 */
	@PostMapping("/delete/{id}")
	public String deleteProduct(@PathVariable Integer id) {
		adminProductService.deleteProduct(id);
		return "redirect:/admin/products";
	}

	/**
	 * ProductForm → Product への変換
	 */
	public Products convertToEntity(ProductForm form, MultipartFile imageFile, Products existingProduct) {
		// 既存の Products が null であれば新しく作成、そうでなければ既存の商品を更新
	    Products product = existingProduct != null ? existingProduct : new Products();
	    
	    // Products product = new Products();
	    product.setId(form.getId()); // IDの設定（編集時に使う）
	    product.setName(form.getName());
	    product.setDescription(form.getDescription());
	    product.setPrice(form.getPrice());
	    product.setStock(form.getStock());
	 
	    Categories category = adminCategoryService.getCategoryById(form.getCategoryId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
		product.setCategory(category);

		if (imageFile != null && !imageFile.isEmpty()) {
			String imagePath = adminProductService.saveImage(imageFile, category);
			product.setImagePath(imagePath);
		} else if (existingProduct != null) {
	        // 画像が変更されていない場合、既存の画像パスを保持
	        product.setImagePath(existingProduct.getImagePath());
	    }

		return product;
	}

	/**
	 * Product → ProductForm への変換（編集画面で初期値表示）
	 */
	public ProductForm convertToForm(Products product) {
		ProductForm form = new ProductForm();
		form.setId(product.getId());
		form.setName(product.getName());
		form.setExistingImagePath(product.getImagePath());
		form.setDescription(product.getDescription());
		form.setPrice(product.getPrice());
		form.setStock(product.getStock());
		form.setCategoryId(product.getCategory().getId());

		return form;
	}

    // 拡張子チェックメソッド
    private boolean isAllowedExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) return false;
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        return List.of("jpg", "jpeg", "png").contains(extension);
    }

}
