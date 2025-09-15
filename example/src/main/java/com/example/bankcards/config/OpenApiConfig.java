package com.example.bankcards.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    /**
     * Создаёт экземпляр OpenAPI для настройки Swagger.
     *
     * @return OpenAPI объект с информацией о API
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank Cards API")
                        .version("1.0")
                        .description("API для управления банковскими картами и переводами."));
                    
    }
}
