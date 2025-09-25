package com.example.bankcards.controller;

import com.example.bankcards.dto.CreateUserDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.service.UserService;
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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable all filters, including JwtAuthenticationFilter
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        // Mock for createUser
        UserDTO createdUserDTO = new UserDTO();
        createdUserDTO.setId(1L);
        createdUserDTO.setUsername("newuser"); // Match the input username
        createdUserDTO.setEnabled(true);

        // Mock for getAllUsers
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("admin");
        userDTO.setEnabled(true);

        when(userService.createUser(any(CreateUserDTO.class))).thenReturn(createdUserDTO);
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(userDTO));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_ShouldReturnCreatedUser() throws Exception {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("newuser");
        dto.setPassword("password123");

        mockMvc.perform(post("/api/users")
                .with(csrf()) // Keep CSRF for POST
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"newuser\",\"password\":\"password123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newuser"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_ShouldReturnUserList() throws Exception {
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin"));
    }
}