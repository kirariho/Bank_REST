package com.example.bankcards.controller;

import com.example.bankcards.security.JwtUtil;
import com.example.bankcards.service.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    // Общий мок UserDetails для JwtUtil
    private UserDetails getMockUser() {
        return User.withUsername("admin")
                .password("password")
                .roles("USER")
                .build();
    }

    private void setupJwtMocks() {
        UserDetails userDetails = getMockUser();
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtUtil.validateToken(anyString(), any(UserDetails.class))).thenReturn(true);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void createCard_returnsOk() throws Exception {
        setupJwtMocks();

        mockMvc.perform(post("/api/cards")
                        .param("ownerId", "1")
                        .param("rawCardNumber", "1234567812341234")
                        .param("expiryDateStr", "2030-12-01")
                        .param("initialBalance", "100.00")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void deleteCard_returnsOk() throws Exception {
        setupJwtMocks();

        mockMvc.perform(delete("/api/cards/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void updateBalance_returnsOk() throws Exception {
        setupJwtMocks();

        mockMvc.perform(put("/api/cards/1/balance")
                        .param("newBalance", "500.00")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void getUserCards_returnsOk() throws Exception {
        setupJwtMocks();

        mockMvc.perform(get("/api/cards/user/1"))
                .andExpect(status().isOk());
    }
}
