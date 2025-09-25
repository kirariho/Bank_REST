package com.example.bankcards.controller;

import com.example.bankcards.dto.CreateUserDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.Role;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.security.JwtUtil;
import com.example.bankcards.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.Set;

/**
 * Контроллер для аутентификации пользователей.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil, RoleRepository roleRepository){
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.roleRepository = roleRepository;

    } 

    /**
     * Регистрирует нового пользователя с ролью USER по умолчанию.
     *
     * @param dto данные для создания пользователя
     * @return ResponseEntity с UserDTO и статусом 201 (Created)
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody CreateUserDTO dto) {
        UserDTO user = userService.createUser(dto);
        // Назначаем роль USER по умолчанию
        Role userRole = roleRepository.findByName("USER");
        if (userRole != null) {
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.getRoles().addAll(roles); // Предполагается, что UserDTO имеет доступ к ролям
            userService.saveUser(user); // Метод для сохранения обновлённого пользователя
        }
        return ResponseEntity.status(201).body(user);
    }

    /**
     * Аутентифицирует пользователя и возвращает JWT-токен.
     *
     * @param username имя пользователя
     * @param password пароль
     * @return ResponseEntity с токеном и статусом 200 (OK)
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }
}