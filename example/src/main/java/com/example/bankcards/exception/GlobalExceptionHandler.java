package com.example.bankcards.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Глобальный обработчик исключений для API.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает пользовательское исключение CardNotFoundException.
     *
     * @param ex исключение
     * @return ResponseEntity с ошибкой и статусом 404
     */
    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCardNotFoundException(CardNotFoundException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", "Card Not Found");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает пользовательское исключение InsufficientBalanceException.
     *
     * @param ex исключение
     * @return ResponseEntity с ошибкой и статусом 400
     */
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientBalanceException(InsufficientBalanceException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", "Isuffisient Balance");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    /**
     * Обрабатывает пользовательское исключение UnauthorizedException.
     *
     * @param ex исключение
     * @return ResponseEntity с ошибкой и статусом 403
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedException(UnauthorizedException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", "Unauthorized");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        
    }

    /**
     * Обрабатывает ошибки валидации (например, @Valid).
     *
     * @param ex исключение валидации
     * @return ResponseEntity с ошибками и статусом 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }

    /**
     * Обрабатывает все остальные необработанные исключения.
     *
     * @param ex общее исключение
     * @return ResponseEntity с ошибкой и статусом 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal Server Error");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
