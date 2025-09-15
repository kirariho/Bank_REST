package com.example.bankcards.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InsufficientBalanceException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.EncryptionUtil;

/**
 * Сервис для работы с банковскими картами.
 * Предоставляет методы для создания, получения, обновления баланса и удаления карт,
 * а также для преобразования сущностей в DTO.
 */
@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Создаёт новую карту для указанного пользователя.
     *
     * @param ownerId        ID владельца карты.
     * @param rawCardNumber  Номер карты в незашифрованном виде.
     * @param expiryDate     Дата окончания действия карты.
     * @param initialBalance Начальный баланс карты.
     * @return DTO созданной карты.
     * @throws RuntimeException если пользователь с указанным ID не найден.
     */
    public CardDTO createCard(Long ownerId, String rawCardNumber, LocalDate expiryDate, BigDecimal initialBalance) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found."));
        Card card = new Card();
        card.setCardNumberEncrypted(EncryptionUtil.encrypt(rawCardNumber));
        card.setOwner(owner);
        card.setExpiryDate(expiryDate);
        card.setBalance(initialBalance);
        card.setStatus(CardStatus.ACTIVE);
        card = cardRepository.save(card);
        return toDto(card);
    }

    /**
     * Получает список всех карт указанного пользователя.
     *
     * @param ownerId ID владельца карт.
     * @return Список DTO карт пользователя.
     */
    public List<CardDTO> getUserCards(Long ownerId) {
        return cardRepository.findByOwnerId(ownerId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Обновляет баланс указанной карты.
     *
     * @param cardId    ID карты.
     * @param newBalance Новый баланс карты.
     * @return DTO обновлённой карты.
     * @throws CardNotFoundException         если карта с указанным ID не найдена.
     * @throws InsufficientBalanceException  если новый баланс отрицательный.
     */
    public CardDTO updateCardBalance(Long cardId, BigDecimal newBalance) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card with ID " + cardId + " not found."));
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException("Balance cannot be negative.");
        }
        card.setBalance(newBalance);
        card = cardRepository.save(card);
        return toDto(card);
    }

    /**
     * Удаляет карту по её ID.
     *
     * @param cardId ID карты.
     * @throws CardNotFoundException если карта не найдена.
     */
    public void deleteCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
        cardRepository.delete(card);
    }

    /**
     * Преобразует сущность Card в DTO.
     *
     * @param card Сущность карты.
     * @return DTO карты.
     */
    public CardDTO toDto(Card card) {
        CardDTO dto = new CardDTO();
        dto.setId(card.getId());
        dto.setMaskedCardNumber(maskCardNumber(card.getCardNumberEncrypted()));
        dto.setOwnerUsername(card.getOwner().getUsername());
        dto.setExpiryDate(card.getExpiryDate().format(DateTimeFormatter.ofPattern("MM/yy")));
        dto.setBalance(card.getBalance());
        dto.setStatus(card.getStatus());
        return dto;
    }

    /**
     * Маскирует номер карты для безопасного отображения.
     *
     * @param encryptedNumber Зашифрованный номер карты.
     * @return Маскированный номер карты.
     */
    private String maskCardNumber(String encryptedNumber) {
        String decrypted = EncryptionUtil.decrypt(encryptedNumber);
        if (decrypted == null || decrypted.length() < 4) {
            return "**** **** **** ****";
        }
        return "**** **** **** " + decrypted.substring(decrypted.length());
    }
}
