package com.example.mgmgapp.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.mgmgapp.entity.Admins;
import com.example.mgmgapp.repository.admin.AdminRepository;

@Configuration
public class AdminInitConfig {

    @Bean
    public CommandLineRunner initAdmin(AdminRepository adminRepository) {
        return args -> {
        	
            // ① パスワードを変数に格納
            String rawPassword = "admin";
            String userName = "admin";

            // ② ハッシュ化
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(rawPassword);

            // ③ エンティティ作成してDBに保存
            Admins admin = new Admins();
            admin.setUserName(userName);
            admin.setPassword(hashedPassword);

            // DBにすでに同名ユーザーがいない場合のみ登録
            if (adminRepository.findByUserName(userName).isEmpty()) {
                adminRepository.save(admin);
                System.out.println("✅ 管理者アカウントを初期登録しました");
            } else {
                System.out.println("ℹ️ ユーザー名 '" + userName + "' は既に登録済みです");
            }
        };
    }
}
