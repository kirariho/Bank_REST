package com.example.bankcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс Spring Boot приложения для управления банковскими картами.
 * Отвечает за запуск приложения и сканирование компонентов в пакете com.example.bankcards.
 */
@SpringBootApplication(scanBasePackages = "com.example.bankcards")
public class BankApplication {

    /**
     * Точка входа в приложение.
     * Запускает Spring Boot контекст.
     *
     * @param args аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }
}
