package com.example.bankcards.dto;

import java.util.Set;

import com.example.bankcards.entity.Role;
import lombok.Data;

/**
 * Data Transfer Object (DTO) для представления информации о пользователе.
 * Используется для передачи данных между слоями приложения и для сериализации в JSON.
 */
@Data
public class UserDTO {

    /**
     * Уникальный идентификатор пользователя.
     */
    private Long id;

    /**
     * Имя пользователя (username).
     */
    private String username;

    /**
     * Статус активности пользователя.
     * true — пользователь активен, false — заблокирован или отключён.
     */
    private boolean enabled;

    /**
     * Набор ролей пользователя.
     * Используется для управления доступом и правами в приложении.
     */
    private Set<Role> roles;
}
