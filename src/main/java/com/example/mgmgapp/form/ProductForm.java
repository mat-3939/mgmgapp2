package com.example.mgmgapp.form;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * 商品情報の入力フォームクラス
 * 管理画面での商品登録・編集に使用する。
 */
@Data
public class ProductForm {
	
	/**
     * 商品ID（主キー）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	/**
     * 商品名（必須、最大30文字）
     * 一意である必要があるため、登録時に重複チェックを行う。
     */
	@NotBlank(message = "商品名を入力してください")
    @Size(max = 30, message = "商品名は30文字以内で入力してください")
    private String name;

    /**
     * 商品説明（任意、最大200文字）
     */
	@Size(max = 200, message = "商品説明は200文字以内で入力してください")
    private String description;

    /**
     * 商品価格（必須、小数点以下2桁まで）
     */
	@NotNull(message = "価格を入力してください")
    @Min(value = 1, message = "価格は1以上を入力してください")
    private Integer price;

    /**
     * 在庫数（必須）
     * デフォルトで100を設定。
     */
    @Min(value = 100, message = "在庫数は100以上を入力してください")
    private Integer stock;

    /**
     * 商品画像（必須）
     * MultipartFileを利用して、画像ファイルをアップロード。
     * 新規登録時は必須、編集時は変更しない場合もあるため、状況に応じてnull許容可。
     */
	@NotNull(message = "商品画像を選択してください")
    private MultipartFile imageFile;
	private String existingImagePath; // 既存の画像ファイル名（編集時用）

    /**
     * カテゴリID（必須）
     * 管理画面でのカテゴリ選択時に、カテゴリのIDを選択肢として取得・送信する想定。
     */
	@NotNull(message = "カテゴリを選択してください")
    private Integer categoryId;
}
