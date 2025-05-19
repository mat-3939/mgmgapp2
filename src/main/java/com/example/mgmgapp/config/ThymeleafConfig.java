package com.example.mgmgapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.mgmgapp.util.PriceFormatter;

@Configuration
public class ThymeleafConfig {
    @Bean
    public PriceFormatter priceFormatter() {
        return new PriceFormatter();
    }
}
