package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Сущность (Entity) пользователя.
 * Содержит информацию о пользователе, его учётные данные и роли.
 */
@Entity
@Table(name = "users")
@Data
public class User {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя (username).
     * Должно быть уникальным.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Пароль пользователя.
     * Хранится в зашифрованном виде (хэшируется в сервисе перед сохранением).
     */
    @Column(nullable = false)
    private String password; 

    /**
     * Статус активности пользователя.
     * true — пользователь активен, false — заблокирован.
     */
    @Column(nullable = false)
    private boolean enabled;

    /**
     * Набор ролей пользователя.
     * Используется для управления доступом и правами в приложении.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
