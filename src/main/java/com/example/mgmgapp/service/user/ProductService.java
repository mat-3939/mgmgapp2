package com.example.mgmgapp.service.user;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.mgmgapp.entity.Categories;
import com.example.mgmgapp.entity.Products;
import com.example.mgmgapp.repository.user.ProductRepository;
import com.example.mgmgapp.util.CategoryDirectoryMapper;

@Service
public class ProductService {
	
	private final ProductRepository productRepository;
    
	@Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
	
	// 商品名で検索（部分一致）
    public List<Products> searchByName(String query) {
        // クエリが空でないかチェック
        if (query == null || query.isEmpty()) {
            return Collections.emptyList(); // 空ならば空のリストを返す
        }

        // 商品名に部分一致する商品を検索
        return productRepository.findByNameContainingIgnoreCase(query);
    }
    
    public List<Products> searchByKeywordAndCategorySorted(String keyword, Integer categoryId, String sort) {
    	Sort sorting;
        switch (sort) {
            case "priceAsc":
                sorting = Sort.by(Sort.Direction.ASC, "price");
                break;
            case "priceDesc":
                sorting = Sort.by(Sort.Direction.DESC, "price");
                break;
            default:
                sorting = Sort.by(Sort.Direction.DESC, "createdAt");
                break;
        }
    	return productRepository.findByNameContainingAndCategoryId(keyword, categoryId, sorting);
    }
    
    /**
     * キーワード検索＆カテゴリ絞り込み
     */
    public List<Products> searchByNameAndCategory(String keyword, Integer categoryId) {
        return productRepository.findByNameContainingAndCategoryId(keyword, categoryId);
    }
    
    /**
     * キーワード検索＆ソート
     */
    public List<Products> searchByKeywordSorted(String keyword, String sort) {
    	Sort sorting;
        switch (sort) {
            case "priceAsc":
                sorting = Sort.by(Sort.Direction.ASC, "price");
                break;
            case "priceDesc":
                sorting = Sort.by(Sort.Direction.DESC, "price");
                break;
            default:
                sorting = Sort.by(Sort.Direction.DESC, "createdAt");
                break;
        }
        return productRepository.findByNameContainingIgnoreCase(keyword, sorting);
    }

    
    /**
     * 指定したカテゴリの商品一覧を取得する
     */
    public List<Products> findByCategoryId(Integer categoryId) {
        return productRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId);
    }
    
    /**
     * ソート付きで商品を取得（ControllerでsortKeyによって分岐）
     */
    public List<Products> getSortedProducts(String sortKey) {
    	return switch (sortKey) {
    	case "priceAsc" -> productRepository.findAllByOrderByPriceAsc();
    	case "priceDesc" -> productRepository.findAllByOrderByPriceDesc();
    	default -> productRepository.findAllByOrderByCreatedAtDesc(); // "new" またはデフォルト
    	};
    }
    
    /**
     * カテゴリ指定＆ソート条件
     */
    public List<Products> findByCategoryIdSorted(Integer categoryId, String sort) {
        Sort sorting;
        switch (sort) {
            case "priceAsc":
                sorting = Sort.by(Sort.Direction.ASC, "price");
                break;
            case "priceDesc":
                sorting = Sort.by(Sort.Direction.DESC, "price");
                break;
            default:
                sorting = Sort.by(Sort.Direction.DESC, "createdAt");
                break;
        }
        return productRepository.findByCategoryId(categoryId, sorting);
    }
    
    public Products getProductById(Integer id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));
    }
    
    /**
     * 画像保存処理
     */
    public String saveImage(MultipartFile imageFile, Categories category) {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        try {
            // カテゴリ名をローマ字に変換
            String categoryRomaji = CategoryDirectoryMapper.toRomaji(category.getName());

            // 保存先ディレクトリの構築
            String uploadDir = "src/main/resources/static/img/" + categoryRomaji;

            // ディレクトリがなければ作成
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // ファイル名の重複回避（タイムスタンプ追加など）
            String originalFileName = imageFile.getOriginalFilename();
            String timestamp = String.valueOf(System.currentTimeMillis());
            String safeFileName = timestamp + "_" + originalFileName;

            // 保存パス
            Path path = Paths.get(uploadDir, safeFileName);
            Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Web上で参照するパスを返す（HTMLで表示する用）
            return "/img/" + categoryRomaji + "/" + safeFileName;

        } catch (IOException e) {
            throw new RuntimeException("画像の保存に失敗しました: " + e.getMessage(), e);
        }
    }
    
    /**
     * 指定した商品IDのリストで複数の商品を取得
     */
    public List<Products> getProductsByIds(List<Integer> ids) {
        return productRepository.findByIdIn(ids);
    }
}
