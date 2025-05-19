package com.example.mgmgapp.controller.user;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mgmgapp.entity.Products;
import com.example.mgmgapp.service.user.CategoryService;
import com.example.mgmgapp.service.user.ProductService;

@Controller
public class ProductController {
	
	private final ProductService productService;
	private final CategoryService categoryService;
	
	@Autowired
	public ProductController(ProductService productService, CategoryService categoryService) {
		this.productService = productService;
		this.categoryService = categoryService;
	} 
	
	@GetMapping("/")
	public String goHome(Model model) {
		//PICK UP商品を表示
        List<Integer> ids = Arrays.asList(1, 6, 8, 11, 3); //任意の商品IDをセット
        List<Products> pickUp = productService.getProductsByIds(ids);
        
        model.addAttribute("pickUp", pickUp);
        
		return "user/index";
	}

    @GetMapping("/products")
    public String showProducts(
    	  @RequestParam(required = false) String q,
    	  @RequestParam(required = false) Integer categoryId,
    	  @RequestParam(required = false, defaultValue = "new") String sort,
          Model model) {
    	
    	List<Products> products;
    	
    	// フィルタリング条件の分岐
    	if (q != null && !q.isEmpty()) {
            if (categoryId != null) {
                products = productService.searchByKeywordAndCategorySorted(q, categoryId, sort);
            } else {
                products = productService.searchByKeywordSorted(q, sort);
            }
        } else {
            if (categoryId != null) {
                products = productService.findByCategoryIdSorted(categoryId, sort);
            } else {
                products = productService.getSortedProducts(sort);
            }
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("sort", sort);
        model.addAttribute("q", q);

        return "user/products";
    }
    
    /**
	 * 指定したカテゴリの商品一覧を表示
	 */
	@GetMapping("/products/category/{categoryId}")
	public String listByCategory(@PathVariable Integer categoryId, Model model) {
		List<Products> products = productService.findByCategoryId(categoryId);
		model.addAttribute("products", products);
		model.addAttribute("searchQuery", null);
		model.addAttribute("categories", categoryService.findAll());
		model.addAttribute("selectedCategoryId", categoryId);
		return "user/products";
	}
    
    //詳細ページへ
    @GetMapping("/products/{id}")
    public String showProductDetail(@PathVariable("id") Integer id, Model model) {
        Products product = productService.getProductById(id);

        //PICK UP商品を表示
        List<Integer> ids = Arrays.asList(1, 6, 8, 11, 3); //任意の商品IDをセット
        List<Products> pickUp = productService.getProductsByIds(ids);

        model.addAttribute("product", product);
        model.addAttribute("pickUp", pickUp);

        return "user/products_detail";
    }
}
