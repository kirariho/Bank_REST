package com.example.bankcards.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bankcards.dto.CreateUserDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Создаёт нового пользователя с ролью USER по умолчанию.
     *
     * @param dto данные для создания пользователя
     * @return UserDTO созданного пользователя
     */
    public UserDTO createUser(CreateUserDTO dto){
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(true);

        Role userRole = roleRepository.findByName("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        user = userRepository.save(user);
        return toDto(user);
    }

    /**
     * Сохраняет обновлённые данные пользователя, включая роли.
     *
     * @param dto данные пользователя для сохранения
     * @return UserDTO сохранённого пользователя
     */
    public UserDTO saveUser(UserDTO dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(dto.getUsername());
        user.setEnabled(dto.isEnabled());
        user.setRoles(dto.getRoles()); // Устанавливаем роли напрямую
        user = userRepository.save(user);
        return toDto(user);
    }

    /**
     * Возвращает список всех пользователей.
     *
     * @return список UserDTO
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Преобразует User в UserDTO.
     *
     * @param user сущность пользователя
     * @return UserDTO
     */
    public UserDTO toDto(User user){
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEnabled(user.isEnabled());

        return dto;
    }

}
