package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью User.
 * Предоставляет стандартные методы CRUD и метод поиска пользователя по имени.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по его имени (username).
     *
     * @param username Имя пользователя.
     * @return Optional, содержащий пользователя, если он найден, иначе пустой.
     */
    Optional<User> findByUsername(String username);
}
