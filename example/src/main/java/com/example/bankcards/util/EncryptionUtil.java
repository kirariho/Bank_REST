package com.example.bankcards.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Утилитный класс для шифрования и дешифрования строк с использованием AES.
 * Секретный ключ загружается из файла `.env` при инициализации класса.
 */
public class EncryptionUtil {

    /**
     * Секретный ключ для AES-шифрования.
     * Загружается из файла `.env` и должен быть длиной 16 символов.
     */
    private static final String SECRET_KEY;

    static {
        try {
            String envContent = new String(Files.readAllBytes(Paths.get(".env")));
            SECRET_KEY = envContent.lines()
                    .filter(line -> line.startsWith("ENCRYPTION_SECRET_KEY="))
                    .map(line -> line.split("=")[1])
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("ENCRYPTION_SECRET_KEY not found in .env"));
            if (SECRET_KEY.length() != 16) {
                throw new IllegalStateException("ENCRYPTION_SECRET_KEY must be 16 characters long");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read .env file", e);
        }
    }

    /**
     * Шифрует переданную строку с использованием AES.
     *
     * @param data Строка для шифрования.
     * @return Зашифрованная строка в формате Base64.
     * @throws RuntimeException если шифрование не удалось.
     */
    public static String encrypt(String data) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed ", e);
        }
    }

    /**
     * Дешифрует переданную строку с использованием AES.
     *
     * @param data Зашифрованная строка в формате Base64.
     * @return Расшифрованная строка.
     * @throws RuntimeException если дешифрование не удалось.
     */
    public static String decrypt(String data) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = Base64.getDecoder().decode(data);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed ", e);
        }
    }
}
