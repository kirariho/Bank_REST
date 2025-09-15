package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    private CardDTO cardDTO;

    @BeforeEach
    void setUp() {
        cardDTO = new CardDTO();
        cardDTO.setId(1L);
        cardDTO.setMaskedCardNumber("**** **** **** 1234");
        cardDTO.setOwnerUsername("admin");
        cardDTO.setExpiryDate(LocalDate.now().plusYears(2).format(DateTimeFormatter.ofPattern("MM/yy"))); // Строка
        cardDTO.setBalance(new BigDecimal("1000.00"));
        cardDTO.setStatus(CardStatus.ACTIVE); // Enum

        when(cardService.createCard(anyLong(), any(String.class), any(LocalDate.class), any(BigDecimal.class))).thenReturn(cardDTO);
        when(cardService.getUserCards(anyLong())).thenReturn(Collections.singletonList(cardDTO));
        when(cardService.updateCardBalance(anyLong(), any(BigDecimal.class))).thenReturn(cardDTO);
    }

    @Test
    @DisplayName("createCard_ShouldReturnCreatedCard")
    void createCard_ShouldReturnCreatedCard() throws Exception {
        mockMvc.perform(post("/api/cards")
                .param("ownerId", "1")
                .param("rawCardNumber", "1234567890123456")
                .param("expiryDateStr", LocalDate.now().plusYears(2).toString())
                .param("initialBalance", "1000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.maskedCardNumber").value("**** **** **** 1234"));
    }

    @Test
    @DisplayName("getUserCards_ShouldReturnCards")
    void getUserCards_ShouldReturnCards() throws Exception {
        mockMvc.perform(get("/api/cards/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].maskedCardNumber").value("**** **** **** 1234"));
    }

    @Test
    @DisplayName("updateCardBalance_ShouldReturnUpdatedCard")
    void updateCardBalance_ShouldReturnUpdatedCard() throws Exception {
        mockMvc.perform(put("/api/cards/1/balance")
                .param("newBalance", "1500")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1500.00));
    }

    @Test
    @DisplayName("deleteCard_ShouldReturnNoContent")
    void deleteCard_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/cards/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}