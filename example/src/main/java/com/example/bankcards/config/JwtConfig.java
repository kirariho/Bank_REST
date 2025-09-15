package com.example.bankcards.config;

import com.example.bankcards.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация свойств JWT.
 */
@Configuration
public class JwtConfig {

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(); // Простая инициализация
    }
}