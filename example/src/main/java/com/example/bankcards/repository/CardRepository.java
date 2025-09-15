package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью Card.
 * Предоставляет стандартные методы CRUD и кастомный метод для поиска карт по владельцу.
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    /**
     * Находит все карты, принадлежащие указанному пользователю.
     *
     * @param ownerId ID владельца карт.
     * @return Список карт пользователя.
     */
    List<Card> findByOwnerId(Long ownerId);
}
