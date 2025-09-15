package com;

import com.example.bankcards.util.EncryptionUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EncryptionUnitTest {

    @Test
    void testEncryptionDecryption() {
        String original = "1234567812345678";
        String encrypted = EncryptionUtil.encrypt(original);
        String decrypted = EncryptionUtil.decrypt(encrypted);
        assertEquals(original, decrypted);
    }
}
