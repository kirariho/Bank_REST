package com.example.bankcards.entity;

import com.example.bankcards.util.EncryptionUtil;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * Сущность (Entity) банковской карты.
 * Содержит информацию о карте, её владельце, балансе и статусе.
 */
@Entity
@Table(name = "cards")
@Data
public class Card {

    /**
     * Уникальный идентификатор карты.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Зашифрованный номер карты.
     * Хранится в базе данных в зашифрованном виде для безопасности.
     */
    @Column(name = "card_number_encrypted", nullable = false, unique = true)
    private String cardNumberEncrypted;

    /**
     * Владелец карты (пользователь).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    /**
     * Дата окончания действия карты.
     */
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    /**
     * Баланс карты.
     */
    @Column(nullable = false)
    private BigDecimal balance;

    /**
     * Статус карты (например, ACTIVE, BLOCKED, EXPIRED).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status;

    /**
     * Метод, вызываемый перед сохранением или обновлением сущности.
     * Автоматически обновляет статус карты:
     * - EXPIRED, если срок действия карты истёк.
     * - ACTIVE, если статус ещё не установлен.
     */
    @PrePersist
    @PreUpdate
    private void updateStatus() {
        LocalDate currentDate = LocalDate.now();

        if (expiryDate != null && expiryDate.isBefore(currentDate)) {
            this.status = CardStatus.EXPIRED;
        } else if (this.status == null) {
            this.status = CardStatus.ACTIVE;
        }
    }

    /**
     * Возвращает маскированный номер карты для отображения пользователю.
     * Например: **** **** **** 1234.
     *
     * @return Маскированный номер карты.
     */
    public String getMaskedNumber() {
        String decrypted = EncryptionUtil.decrypt(cardNumberEncrypted);
        if (decrypted == null || decrypted.length() < 4) {
            return "****";
        }
        return "**** **** **** " + decrypted.substring(decrypted.length() - 4);
    }
}
