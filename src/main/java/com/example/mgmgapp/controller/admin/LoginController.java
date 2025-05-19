package com.example.mgmgapp.controller.admin;

import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.mgmgapp.form.LoginForm;

@Controller
@RequestMapping("/login")
public class LoginController {

    // 管理者ログインページの表示
    @GetMapping
    public String showLogin(@ModelAttribute LoginForm form) {
        return "admin/login"; // → templates/admin/login.html
    }

    // ログイン成功後の遷移先（商品一覧ページ）
    // @PostMapping("/login")
    // public String doLogin(@ModelAttribute LoginForm form, Model model) {
    //     return "redirect:/admin/products";
    // }
}
