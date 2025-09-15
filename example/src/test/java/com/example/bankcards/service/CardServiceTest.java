package com.example.bankcards.service;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InsufficientBalanceException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.EncryptionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CardService cardService;

    private Card card;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("admin");

        card = new Card();
        card.setId(1L);
        card.setCardNumberEncrypted("encrypted123");
        card.setOwner(user);
        card.setExpiryDate(LocalDate.now().plusYears(2));
        card.setBalance(new BigDecimal("1000.00"));
        card.setStatus(CardStatus.ACTIVE);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.findByOwnerId(1L)).thenReturn(java.util.Collections.singletonList(card));
        when(EncryptionUtil.encrypt(any(String.class))).thenReturn("encrypted123");
        when(EncryptionUtil.decrypt("encrypted123")).thenReturn("1234567890123456");
    }

    @Test
    void createCard_ShouldReturnCardDTO() {
        CardDTO dto = cardService.createCard(1L, "1234567890123456", LocalDate.now().plusYears(2), new BigDecimal("1000.00"));
        assertEquals("**** **** **** 3456", dto.getMaskedCardNumber());
        assertEquals(new BigDecimal("1000.00"), dto.getBalance());
    }

    @Test
    void getUserCards_ShouldReturnCardDTOs() {
        List<CardDTO> dtos = cardService.getUserCards(1L);
        assertEquals(1, dtos.size());
        assertEquals("**** **** **** 3456", dtos.get(0).getMaskedCardNumber());
    }

    @Test
    void updateCardBalance_ShouldUpdateBalance() {
        CardDTO dto = cardService.updateCardBalance(1L, new BigDecimal("1500.00"));
        assertEquals(new BigDecimal("1500.00"), dto.getBalance());
    }

    @Test
    void updateCardBalance_ShouldThrowExceptionForNegativeBalance() {
        assertThrows(InsufficientBalanceException.class, () -> cardService.updateCardBalance(1L, new BigDecimal("-100")));
    }

    @Test
    void deleteCard_ShouldNotThrowException() {
        cardService.deleteCard(1L); // Ожидаем, что не будет исключений
    }

    @Test
    void updateCardBalance_ShouldThrowCardNotFoundException() {
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(CardNotFoundException.class, () -> cardService.updateCardBalance(2L, new BigDecimal("1000")));
    }
}