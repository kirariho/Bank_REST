package com.example.bankcards.controller;

import com.example.bankcards.dto.CreateUserDTO;
import com.example.bankcards.entity.Role;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.security.JwtUtil;
import com.example.bankcards.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        when(authenticationManager.authenticate(any())).thenReturn(null); // Симуляция успешной аутентификации
        when(jwtUtil.generateToken(any())).thenReturn("mocked-jwt-token");

        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setName("USER");
        when(roleRepository.findByName("USER")).thenReturn(userRole);
    }

    @Test
    @DisplayName("login_ShouldReturnToken")
    void login_ShouldReturnToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .param("username", "admin")
                .param("password", "admin123")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")) // Имитация Basic Auth
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("mocked-jwt-token"));
    }

    @Test
    @DisplayName("registerUser_ShouldReturnCreatedUser")
    void registerUser_ShouldReturnCreatedUser() throws Exception {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("newuser");
        dto.setPassword("password123");

        com.example.bankcards.dto.UserDTO userDTO = new com.example.bankcards.dto.UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("newuser");
        userDTO.setEnabled(true);
        Set<Role> roles = new HashSet<>();
        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setName("USER");
        roles.add(userRole);
        userDTO.setRoles(roles);

        when(userService.createUser(dto)).thenReturn(userDTO);
        when(userService.saveUser(userDTO)).thenReturn(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"newuser\",\"password\":\"password123\"}")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123"))) // Имитация Basic Auth для регистрации
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.roles[0].name").value("USER"));
    }
}