package com.example.bankcards.repository;

import com.example.bankcards.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью Role.
 * Предоставляет стандартные методы CRUD и метод поиска роли по имени.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Находит роль по её имени.
     *
     * @param name Название роли (например, "ROLE_USER", "ROLE_ADMIN").
     * @return Объект Role с указанным именем или null, если не найдено.
     */
    Role findByName(String name);
}
