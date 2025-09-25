package com.example.bankcards.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bankcards.dto.CreateUserDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Создаёт нового пользователя с ролью USER по умолчанию.
     *
     * @param dto данные для создания пользователя
     * @return UserDTO созданного пользователя
     */
    public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
    }

    @Transactional
    public UserDTO createUser(CreateUserDTO dto){
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(true);

        Role userRole = roleRepository.findByName("USER");
        if(userRole == null){
            throw new RoleNotFoundException("Role USER not found");
        }
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
    @Transactional
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
        dto.setRoles(user.getRoles());
        
        return dto;
    }

}
