package com.example.mgmgapp.service.admin;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.mgmgapp.entity.Categories;
import com.example.mgmgapp.entity.Products;
import com.example.mgmgapp.repository.admin.AdminProductRepository;
import com.example.mgmgapp.util.CategoryDirectoryMapper;

/**
 * 管理者側の商品操作サービスクラス。
 * 商品の一覧取得、詳細取得、登録、更新、削除などのロジックを定義。
 */
@Service
public class AdminProductService {

    private final AdminProductRepository adminProductRepository;

    @Autowired
    public AdminProductService(AdminProductRepository adminProductRepository) {
        this.adminProductRepository = adminProductRepository;
    }

    /**
     * 全商品の一覧を取得（登録日が新しい順）
     */
    public List<Products> getAllProducts() {
        return adminProductRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * 商品IDを指定して商品を1件取得
     *
     * @param id 商品ID
     * @return 商品情報（見つからなければ空）
     */
    public Optional<Products> getProductById(Integer id) {
        return adminProductRepository.findById(id);
    }

    /**
     * 商品名が一致する商品を1件取得（ユニークチェックなどに使用）
     *
     * @param name 商品名
     * @return 商品情報（存在すれば）
     */
    public Optional<Products> getProductByName(String name) {
        return adminProductRepository.findByName(name);
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
    	return adminProductRepository.findByNameContainingAndCategoryId(keyword, categoryId, sorting);
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
        return adminProductRepository.findByNameContainingIgnoreCase(keyword, sorting);
    }

    /**
     * 商品を新規登録する
     *
     * @param product 登録する商品エンティティ
     * @return 登録後の商品エンティティ
     */
    @Transactional
    public Products createProduct(Products product) {
        return adminProductRepository.save(product);
    }

    /**
     * 商品を更新する（saveはID付きで呼べばUPDATE動作になる）
     *
     * @param product 更新対象の商品エンティティ
     * @return 更新後の商品
     */
    @Transactional
    public Products updateProduct(Products product) {
        return adminProductRepository.save(product);
    }

    /**
     * 商品を削除する
     *
     * @param id 削除する商品ID
     */
    @Transactional
    public void deleteProduct(Integer id) {
    	adminProductRepository.deleteById(id);
    }

    /**
     * 部分一致検索（名前）
     *
     * @param keyword キーワード
     * @return 一致する商品一覧
     */
    public List<Products> searchByKeyword(String keyword) {
        return adminProductRepository.findByNameContaining(keyword);
    }
    
    /**
     * 指定したカテゴリの商品一覧を取得する
     */
    public List<Products> findByCategoryId(Integer categoryId) {
        return adminProductRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId);
    }

    /**
     * 指定された価格帯の商品を検索する
     */
    public List<Products> searchByPriceRange(BigDecimal min, BigDecimal max) {
        return adminProductRepository.findByPriceBetween(min, max);
    }

    /**
     * ソート付きで商品を取得（ControllerでsortKeyによって分岐）
     */
    public List<Products> getSortedProducts(String sortKey) {
        return switch (sortKey) {
            case "priceAsc" -> adminProductRepository.findAllByOrderByPriceAsc();
            case "priceDesc" -> adminProductRepository.findAllByOrderByPriceDesc();
            case "name" -> adminProductRepository.findAllByOrderByNameAsc();
            case "update" -> adminProductRepository.findAllByOrderByUpdatedAtDesc();
            default -> adminProductRepository.findAllByOrderByCreatedAtDesc(); // "new" またはデフォルト
        };
    }
    
    /**
     * カテゴリ指定＆ソート条件
     */
    public List<Products> findByCategoryIdSorted(Integer categoryId, String sort) {
        Sort sorting;
        switch (sort) {
            case "update":
                sorting = Sort.by(Sort.Direction.DESC, "updatedAt");
                break;
            case "name":
                sorting = Sort.by(Sort.Direction.ASC, "name");
                break;
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
        return adminProductRepository.findByCategoryId(categoryId, sorting);
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
     * 商品名または商品IDで検索
     *
     * @param keyword キーワード
     * @return 一致する商品一覧
     */
    public List<Products> searchByIdOrName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return adminProductRepository.findAll(Sort.by(Sort.Order.desc("createdAt")));
        }
        
        // 数値としてパースできるかチェック（ID検索）
        try {
            Integer id = Integer.parseInt(keyword.trim());
            Optional<Products> productOpt = adminProductRepository.findById(id);
            if (productOpt.isPresent()) {
                return List.of(productOpt.get());
            }
        } catch (NumberFormatException ignored) {
            // 無視して名前検索へ
        }

        // 名前の部分一致検索
        List<Products> nameMatches = adminProductRepository.findByNameContaining(keyword);
        if (nameMatches.isEmpty()) {
            // 該当なし → 全件表示（登録日降順）
            return adminProductRepository.findAll(Sort.by(Sort.Order.desc("createdAt")));
        }

        return nameMatches;
    }

}
