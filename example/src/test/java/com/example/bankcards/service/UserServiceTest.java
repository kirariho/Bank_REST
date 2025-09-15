package com.example.bankcards.service;

import com.example.bankcards.dto.CreateUserDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private Role userRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("encodedPassword");
        user.setEnabled(true);

        userRole = new Role();
        userRole.setId(1L);
        userRole.setName("USER");

        when(roleRepository.findByName("USER")).thenReturn(userRole);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
    }

    @Test
    void createUser_ShouldReturnUserDTO() {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("newuser");
        dto.setPassword("password123");

        UserDTO userDTO = userService.createUser(dto);
        assertEquals("newuser", userDTO.getUsername());
        assertEquals(true, userDTO.isEnabled());
    }

    @Test
    void saveUser_ShouldReturnUpdatedUserDTO() {
        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setUsername("updatedAdmin");
        dto.setEnabled(false);
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        dto.setRoles(roles);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO updatedDTO = userService.saveUser(dto);
        assertEquals("updatedAdmin", updatedDTO.getUsername());
        assertEquals(false, updatedDTO.isEnabled());
    }

    @Test
    void getAllUsers_ShouldReturnUserList() {
        when(userRepository.findAll()).thenReturn(java.util.Collections.singletonList(user));
        List<UserDTO> users = userService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("admin", users.get(0).getUsername());
    }
}