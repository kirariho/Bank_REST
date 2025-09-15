package com.example.bankcards.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankcards.dto.CreateUserDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.service.UserService;

import jakarta.validation.Valid;

/**
 * REST-контроллер для управления пользователями.
 * Предоставляет эндпоинты для создания нового пользователя и получения списка всех пользователей.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Создаёт нового пользователя.
     *
     * @param dto Объект CreateUserDTO с данными нового пользователя.
     * @return ResponseEntity с созданным пользователем и статусом 201 CREATED.
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO dto) {
        UserDTO createdUser = userService.createUser(dto);
        return ResponseEntity.status(201).body(createdUser);
    }

    /**
     * Получает список всех пользователей.
     *
     * @return ResponseEntity со списком пользователей и статусом 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
