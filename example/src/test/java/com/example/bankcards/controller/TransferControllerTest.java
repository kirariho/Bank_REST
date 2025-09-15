package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.exception.InsufficientBalanceException;
import com.example.bankcards.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TransferController.class)
public class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    private CardDTO fromCard;
    private CardDTO toCard;

    @BeforeEach
    void setUp() {
        fromCard = new CardDTO();
        fromCard.setId(1L);
        fromCard.setBalance(new BigDecimal("1000.00"));

        toCard = new CardDTO();
        toCard.setId(2L);
        toCard.setBalance(new BigDecimal("500.00"));
    }

    @Test
    void transferMoney_ShouldSucceedWithSufficientBalance() throws Exception {
        when(cardService.getUserCards(1L)).thenReturn(java.util.Collections.singletonList(fromCard));
        when(cardService.getUserCards(2L)).thenReturn(java.util.Collections.singletonList(toCard));
        when(cardService.updateCardBalance(1L, new BigDecimal("900.00"))).thenReturn(fromCard);
        when(cardService.updateCardBalance(2L, new BigDecimal("600.00"))).thenReturn(toCard);

        mockMvc.perform(post("/api/transfers")
                .param("fromCardId", "1")
                .param("toCardId", "2")
                .param("amount", "100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromCard.balance").value(900.00))
                .andExpect(jsonPath("$.toCard.balance").value(600.00));
    }

    @Test
    void transferMoney_ShouldFailWithInsufficientBalance() throws Exception {
        when(cardService.getUserCards(1L)).thenReturn(java.util.Collections.singletonList(fromCard));

        mockMvc.perform(post("/api/transfers")
                .param("fromCardId", "1")
                .param("toCardId", "2")
                .param("amount", "2000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // Предполагаем обработку исключения
    }
}