package com.example.mgmgapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        	.securityMatcher(new AntPathRequestMatcher("/**"))
            .authorizeHttpRequests(authz -> authz
            	.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
            	.requestMatchers("/", "/index", "/about", "/contact").permitAll()
            	.anyRequest().permitAll()
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
