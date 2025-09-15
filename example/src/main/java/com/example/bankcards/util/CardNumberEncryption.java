package com.example.bankcards.util;

/**
 * Утилитный класс для шифрования номеров банковских карт.
 * Содержит пример использования {@link EncryptionUtil} для демонстрации шифрования.
 */
public class CardNumberEncryption {

    /**
     * Точка входа в программу.
     * Шифрует два номера карт и выводит зашифрованные значения в консоль.
     *
     * @param args аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        String card1 = EncryptionUtil.encrypt("2202222234565555");
        String card2 = EncryptionUtil.encrypt("2201212134549080");
        System.out.println("Encrypted card 1: " + card1);
        System.out.println("Encrypted card 2: " + card2);
    }
}
