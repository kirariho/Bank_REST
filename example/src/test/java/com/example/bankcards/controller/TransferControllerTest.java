package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.bankcards.security.JwtUtil;
import com.example.bankcards.security.JwtAuthenticationFilter;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TransferController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private UserDetailsService userDetailsService;

    private CardDTO fromCard;
    private CardDTO toCard;

    @BeforeEach
    void setUp() {
        fromCard = new CardDTO();
        fromCard.setId(1L);
        fromCard.setBalance(new BigDecimal("1000.00"));
        fromCard.setMaskedCardNumber("1234-5678-9012-3456");
        fromCard.setOwnerUsername("user1");
        fromCard.setStatus(CardStatus.ACTIVE);
        fromCard.setExpiryDate("12/25");

        toCard = new CardDTO();
        toCard.setId(2L);
        toCard.setBalance(new BigDecimal("500.00"));
        toCard.setMaskedCardNumber("9876-5432-1098-7654");
        toCard.setOwnerUsername("user2");
        toCard.setStatus(CardStatus.ACTIVE);
        toCard.setExpiryDate("12/25");
    }

@Test
@WithMockUser(roles = "ADMIN")
void transferMoney_ShouldSucceedWithSufficientBalance() throws Exception {

    CardDTO updatedFromCard = new CardDTO();
    updatedFromCard.setId(1L);
    updatedFromCard.setBalance(new BigDecimal("900.00"));
    updatedFromCard.setMaskedCardNumber("1234-5678-9012-3456");
    updatedFromCard.setOwnerUsername("user1");
    updatedFromCard.setStatus(CardStatus.ACTIVE);
    updatedFromCard.setExpiryDate("12/25");

    CardDTO updatedToCard = new CardDTO();
    updatedToCard.setId(2L);
    updatedToCard.setBalance(new BigDecimal("600.00"));
    updatedToCard.setMaskedCardNumber("9876-5432-1098-7654");
    updatedToCard.setOwnerUsername("user2");
    updatedToCard.setStatus(CardStatus.ACTIVE);
    updatedToCard.setExpiryDate("12/25");

    List<CardDTO> fromCardList = Collections.singletonList(fromCard);
    List<CardDTO> toCardList = Collections.singletonList(toCard);
    
    when(cardService.getUserCards(1L)).thenReturn(fromCardList);
    when(cardService.getUserCards(2L)).thenReturn(toCardList);
    
    when(cardService.updateCardBalance(1L, new BigDecimal("900.00"))).thenReturn(updatedFromCard);
    when(cardService.updateCardBalance(2L, new BigDecimal("600.00"))).thenReturn(updatedToCard);

    mockMvc.perform(post("/api/transfers")
            .param("fromCardId", "1")
            .param("toCardId", "2")
            .param("amount", "100.00")
            .with(csrf())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fromCard.balance").value(900.00))
            .andExpect(jsonPath("$.toCard.balance").value(600.00));
}

  @Test
@WithMockUser(roles = "ADMIN")
void transferMoney_ShouldFailWithInsufficientBalance() throws Exception {
    when(cardService.getUserCards(1L)).thenReturn(java.util.Collections.singletonList(fromCard));
    when(cardService.getUserCards(2L)).thenReturn(java.util.Collections.singletonList(toCard));

    mockMvc.perform(post("/api/transfers")
            .param("fromCardId", "1")
            .param("toCardId", "2")
            .param("amount", "2000.00")
            .with(csrf())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isBadRequest()) // Изменено с is5xxServerError()
            .andExpect(jsonPath("$.message").value("Insufficient balance for transfer."));
}
}