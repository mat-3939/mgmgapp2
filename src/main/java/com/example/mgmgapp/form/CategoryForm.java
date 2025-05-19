package com.example.mgmgapp.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * 管理者用カテゴリフォーム
 * カテゴリの新規登録・編集に使用
 */
@Data
public class CategoryForm {

	/**
     * カテゴリ名（必須、最大200文字）
     */
    @NotBlank(message = "カテゴリ名は必須です")
    @Size(max = 200, message = "カテゴリ名は200文字以内で入力してください")
    private String name;

}
