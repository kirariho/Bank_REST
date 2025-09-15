package com.example.bankcards.dto;

import java.math.BigDecimal;
import com.example.bankcards.entity.CardStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

/**
 * Data Transfer Object (DTO) для представления информации о банковской карте.
 * Используется для передачи данных между слоями приложения и для сериализации в JSON.
 */
@Data
public class CardDTO {

    /**
     * Уникальный идентификатор карты.
     */
    private Long id; 

    /**
     * Маскированный номер карты (например, **** **** **** 1234).
     */
    private String maskedCardNumber;

    /**
     * Имя пользователя-владельца карты.
     */
    private String ownerUsername; 

    /**
     * Дата окончания действия карты в формате MM/yy.
     */
    @JsonFormat(pattern = "MM/yy") 
    private String expiryDate;

    /**
     * Баланс карты.
     * Не может быть null и должен быть больше или равен нулю.
     */
    @NotNull(message = "Balance cannot be null")
    @PositiveOrZero(message = "Balance must be zero or positive")
    private BigDecimal balance;

    /**
     * Статус карты (например, ACTIVE, BLOCKED).
     * Не может быть null.
     */
    @NotNull(message = "Status cannot be null")
    private CardStatus status; 
}
