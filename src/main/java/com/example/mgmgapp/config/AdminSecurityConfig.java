package com.example.mgmgapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AdminSecurityConfig {
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt でパスワードをエンコード
    }

    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
        	.securityMatcher("/login","/admin/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/js/**").permitAll() //管理者ログインと静的リソースは許可
                // .requestMatchers("/admin/products").authenticated() // 管理者ログイン後、/admin/products は認証されたユーザーだけにアクセス許可
                .requestMatchers("/admin/**").authenticated() // 管理者ログイン済の場合のみアクセス可
                .anyRequest().denyAll() // 他のURLはすべて拒否
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")         //form の action と一致させる　　
                .usernameParameter("user_nameInput")            //フォーム側の name 属性
                .passwordParameter("passwordInput")            //フォーム側の name 属性
                .defaultSuccessUrl("/admin", true)    //成功時 
                .failureUrl("/login?error")              //失敗時
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
            );
        return http.build();
    }
}

