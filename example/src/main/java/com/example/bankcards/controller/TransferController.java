package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.exception.InsufficientBalanceException;
import com.example.bankcards.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST-контроллер для управления денежными переводами между банковскими картами.
 * Предоставляет эндпоинт для перевода средств с одной карты на другую.
 */
@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    
    private final CardService cardService;
    public TransferController(CardService cardService){
        this.cardService = cardService;
    }

    /**
     * Выполняет перевод денежных средств между двумя картами.
     *
     * @param fromCardId ID карты-отправителя.
     * @param toCardId   ID карты-получателя.
     * @param amount     Сумма перевода.
     * @return ResponseEntity с информацией о состоянии обеих карт после перевода.
     * @throws InsufficientBalanceException если баланс карты-отправителя меньше суммы перевода.
     * @throws RuntimeException если исходная или целевая карта не найдены.
     */
    @PostMapping
public ResponseEntity<TransferResponse> transferMoney(
        @RequestParam("fromCardId") Long fromCardId,
        @RequestParam("toCardId") Long toCardId,
        @RequestParam("amount") BigDecimal amount) {

        CardDTO fromCard = cardService.getUserCards(fromCardId).stream()
                .filter(c -> c.getId().equals(fromCardId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Source card not found."));

        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for transfer.");
        }

        CardDTO toCard = cardService.getUserCards(toCardId).stream()
                .filter(c -> c.getId().equals(toCardId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Destination card not found."));

        fromCard = cardService.updateCardBalance(fromCardId, fromCard.getBalance().subtract(amount));
        toCard = cardService.updateCardBalance(toCardId, toCard.getBalance().add(amount));

        TransferResponse response = new TransferResponse(fromCard, toCard);
        return ResponseEntity.ok(response);
    }

    /**
     * Вспомогательный класс-ответ для перевода.
     * Содержит обновлённые данные карты-отправителя и карты-получателя.
     */
    record TransferResponse(CardDTO fromCard, CardDTO toCard) {}
}
