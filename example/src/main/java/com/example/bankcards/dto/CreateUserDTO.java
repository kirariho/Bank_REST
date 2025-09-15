package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object (DTO) для создания нового пользователя.
 * Используется для передачи данных при регистрации или создании пользователя через API.
 */
@Data
public class CreateUserDTO {

    /**
     * Имя пользователя (username).
     * Не может быть пустым и должно содержать от 3 до 50 символов.
     */
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    /**
     * Пароль пользователя.
     * Не может быть пустым и должен содержать от 6 до 255 символов.
     * Будет хэшироваться в сервисе перед сохранением.
     */
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String password;
}
