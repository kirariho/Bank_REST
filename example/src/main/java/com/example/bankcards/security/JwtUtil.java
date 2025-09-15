package com.example.bankcards.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Утилита для работы с JWT-токенами.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}") // Загружаем из application.properties
    private String SECRET_KEY;

    @Value("${jwt.expiration:86400000}") // 24 часа по умолчанию
    private long JWT_EXPIRATION;

    /**
     * Извлекает имя пользователя из токена.
     *
     * @param token JWT-токен
     * @return имя пользователя
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Извлекает произвольное значение из токена.
     *
     * @param token JWT-токен
     * @param claimsResolver функция извлечения
     * @return значение
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Извлекает все claims из токена.
     *
     * @param token JWT-токен
     * @return Claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Генерирует новый JWT-токен.
     *
     * @param userDetails данные пользователя
     * @return токен
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Создаёт токен с указанными claims.
     *
     * @param claims дополнительные данные
     * @param subject имя пользователя
     * @return токен
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }

    /**
     * Проверяет валидность токена.
     *
     * @param token токен
     * @param userDetails данные пользователя
     * @return true, если токен валиден
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Проверяет, истёк ли токен.
     *
     * @param token токен
     * @return true, если токен истёк
     */
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}