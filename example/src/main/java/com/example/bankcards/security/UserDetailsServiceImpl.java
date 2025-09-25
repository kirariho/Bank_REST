package com.example.bankcards.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;

/**
 * Сервис для загрузки данных пользователя для Spring Security с поддержкой ролей.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserRepository userRepository;
    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * Загружает пользователя по имени для аутентификации.
     *
     * @param username имя пользователя
     * @return UserDetails объект с данными пользователя и ролями
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
                
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles().stream()
                            .map(role -> role.getName().toUpperCase())
                            .toArray(String[]::new))
                    .build();

    }
}
