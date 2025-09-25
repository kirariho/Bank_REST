package com.example.bankcards.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.service.CardService;

/**
 * REST-контроллер для управления банковскими картами.
 * Предоставляет эндпоинты для создания, получения, обновления баланса и удаления карт.
 */
@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController (CardService cardService){
        this.cardService = cardService;
    }

    /**
     * Создаёт новую банковскую карту для указанного пользователя.
     *
     * @param ownerId ID владельца карты.
     * @param rawCardNumber Номер карты в виде строки.
     * @param expiryDateStr Дата окончания действия карты в формате ISO_LOCAL_DATE (yyyy-MM-dd).
     * @param initialBalance Начальный баланс карты.
     * @return ResponseEntity с объектом созданной карты и статусом 201 CREATED.
     */
    @PostMapping
    public ResponseEntity<CardDTO> createCard(
        @RequestParam("ownerId") Long ownerId,
        @RequestParam("rawCardNumber") String rawCardNumber,
        @RequestParam("expiryDateStr") String expiryDateStr,
        @RequestParam("initialBalance") BigDecimal initialBalance) {
        
        LocalDate expiryDate = LocalDate.parse(expiryDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        CardDTO card = cardService.createCard(ownerId, rawCardNumber, expiryDate, initialBalance);
        return ResponseEntity.status(201).body(card);
    }

    /**
     * Получает список всех карт указанного пользователя.
     *
     * @param ownerId ID владельца карт.
     * @return ResponseEntity со списком карт пользователя и статусом 200 OK.
     */
    @GetMapping("/user/{ownerId}")
    public ResponseEntity<List<CardDTO>> getUserCards(@PathVariable Long ownerId) {
        List<CardDTO> cards = cardService.getUserCards(ownerId);
        return ResponseEntity.ok(cards);
    }

    /**
     * Обновляет баланс указанной карты.
     *
     * @param cardId ID карты, баланс которой нужно обновить.
     * @param newBalance Новый баланс карты.
     * @return ResponseEntity с обновлённой картой и статусом 200 OK.
     */
    @PutMapping("/{cardId}/balance")
    public ResponseEntity<CardDTO> updateCardBalance(@PathVariable Long cardId, @RequestParam BigDecimal newBalance) {
        CardDTO updatedCard = cardService.updateCardBalance(cardId, newBalance);
        return ResponseEntity.ok(updatedCard);
    }

    /**
     * Удаляет указанную карту.
     *
     * @param cardId ID карты для удаления.
     * @return ResponseEntity с пустым телом и статусом 204 NO CONTENT.
     */
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }
}
