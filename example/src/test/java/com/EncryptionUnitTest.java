package com;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionUnitTest {

    // Мини-утилита для теста
    static class FakeEncryptionUtil {
        static String encrypt(String data) {
            return "fake_" + data;
        }
        static String decrypt(String data) {
            return data.replaceFirst("^fake_", "");
        }
    }

    @Test
    void testEncryptionDecryption() {
        String original = "1234567812345678";
        String encrypted = FakeEncryptionUtil.encrypt(original);
        String decrypted = FakeEncryptionUtil.decrypt(encrypted);
        assertEquals(original, decrypted);
    }
}
