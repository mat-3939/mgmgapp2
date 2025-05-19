package com.example.mgmgapp.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

@Data
public class OrderForm {

    @NotBlank(message = "姓を入力してください")
    private String lastName;

    @NotBlank(message = "名を入力してください")
    private String firstName;

    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "有効なメールアドレスを入力してください")
    private String email;

    @NotBlank(message = "郵便番号を入力してください")
    @Pattern(regexp = "^[0-9]{3}-?[0-9]{4}$", message = "郵便番号の形式が正しくありません")
    private String postcode;


    
    @NotBlank(message = "都道府県を入力してください")
    private String prefecture;

    @NotBlank(message = "市区町村を入力してください")
    private String city;

    @NotBlank(message = "住所を入力してください")
    private String addressLine;

    private String building; // 建物名は任意なので @NotBlank なし


    @Pattern(regexp = "^$|^[0-9]{10,11}$", message = "電話番号は10～11桁の数字で入力してください")
    private String tel;


    private String payMethod;
    
    @NotBlank(message = "カード番号を入力してください")
    private String cardNumber;

    @NotBlank(message = "カード有効期限を入力してください")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$", message = "有効期限はMM/YY形式で入力してください")
    private String cardExpiry;


    @NotBlank(message = "セキュリティコードを入力してください")
    private String cardCvv;
    
    @NotBlank(message = "カード名義人を入力してください")
    private String cardHolderName;
    
    private Boolean status;

}
