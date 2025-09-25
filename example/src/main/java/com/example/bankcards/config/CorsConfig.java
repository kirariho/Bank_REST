package com.example.bankcards.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

   /**
     * Настраивает CORS для всех эндпоинтов.
     *
     * @return WebMvcConfigurer с настройками CORS
     */
    @Bean
    public WebMvcConfigurer carsConfigurer(){
        return new WebMvcConfigurer(){
            @SuppressWarnings("null")
            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    } 
}
