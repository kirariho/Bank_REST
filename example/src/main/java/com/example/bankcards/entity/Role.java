package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Сущность (Entity) роли пользователя.
 * Определяет права доступа и уровень полномочий пользователя в приложении.
 */
@Entity
@Table(name = "roles")
@Data
public class Role {

    /**
     * Уникальный идентификатор роли.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название роли (например, "ROLE_USER", "ROLE_ADMIN").
     * Должно быть уникальным.
     */
    @Column(nullable = false, unique = true)
    private String name;
}
