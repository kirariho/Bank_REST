package com.example.bankcards.controller;

import com.example.bankcards.dto.CreateUserDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.Role;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.security.JwtUtil;
import com.example.bankcards.security.SecurityConfig;
import com.example.bankcards.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

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

    @MockBean
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        // Мок для аутентификации в /login
        UserDetails userDetails = User.withUsername("admin")
                .password("admin123")
                .roles("ADMIN")
                .build();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        when(jwtUtil.generateToken(userDetails)).thenReturn("mocked-jwt-token");
        when(jwtUtil.extractUsername(anyString())).thenReturn("admin"); // Мок для JwtAuthenticationFilter

        // Мок для UserDetailsService (для JwtAuthenticationFilter)
        when(userDetailsService.loadUserByUsername("admin")).thenReturn(userDetails);

        // Мок для роли USER в /register
        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setName("USER");
        when(roleRepository.findByName("USER")).thenReturn(userRole);

        // Мок для создания и сохранения пользователя в /register
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("newuser");
        userDTO.setEnabled(true);
        userDTO.setRoles(new HashSet<>()); // Инициализируем roles явно
        when(userService.createUser(any(CreateUserDTO.class))).thenReturn(userDTO);
        when(userService.saveUser(any(UserDTO.class))).thenReturn(userDTO);
    }

    @Test
    @DisplayName("login_ShouldReturnToken")
    void login_ShouldReturnToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .param("username", "admin")
                .param("password", "admin123")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(content().string("mocked-jwt-token"));
    }

    @Test
    @DisplayName("registerUser_ShouldReturnCreatedUser")
    void registerUser_ShouldReturnCreatedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"newuser\",\"password\":\"password123\"}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("newuser"))
            .andExpect(jsonPath("$.roles[0].name").value("USER"));
    }
}