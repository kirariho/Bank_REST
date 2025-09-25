package com.example.bankcards.service;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InsufficientBalanceException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.EncryptionUtil;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CardService cardService;

    private Card card;
    private User user;

    // Имя временного .env (в рабочем каталоге теста)
    private static final Path TEMP_ENV = Path.of(".env");

    /**
     * Создаём временный .env с валидным 16-символьным ключом,
     * чтобы статический блок EncryptionUtil успешно инициализировался.
     */
    @BeforeAll
    static void createTempEnv() throws IOException {
        if (!Files.exists(TEMP_ENV)) {
            // ровно 16 символов
            String content = "ENCRYPTION_SECRET_KEY=1234567890ABCDEF";
            Files.writeString(TEMP_ENV, content);
            System.out.println("[TEST-SETUP] Temporary .env created for tests");
        } else {
            System.out.println("[TEST-SETUP] .env already exists - will use existing file");
        }
    }

    /**
     * Удаляем временный .env, если мы его создали.
     * (Если у тебя реальный .env в проекте — он не будет удалён,
     * потому что проверяем существование и не перезаписываем реальный файл.)
     */
    @AfterAll
    static void removeTempEnv() throws IOException {
        if (Files.exists(TEMP_ENV)) {
            String fileContent = Files.readString(TEMP_ENV);
            if (fileContent.contains("1234567890ABCDEF")) {
                Files.delete(TEMP_ENV);
                System.out.println("[TEST-TEARDOWN] Temporary .env removed");
            } else {
                System.out.println("[TEST-TEARDOWN] .env left intact (not test-created)");
            }
        }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Подготовка данных
        user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setEnabled(true);

        card = new Card();
        card.setId(1L);
        // используем то же поле, что в твоём сервисе
        card.setCardNumberEncrypted("encrypted123");
        card.setOwner(user);
        card.setBalance(new BigDecimal("1000.00"));
        card.setExpiryDate(LocalDate.now().plusYears(2));
        card.setStatus(CardStatus.ACTIVE);

        // Репозитории
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.findByOwnerId(1L)).thenReturn(Collections.singletonList(card));
    }

    @Test
    void createCard_ShouldReturnCardDTO() {
        try (MockedStatic<EncryptionUtil> encryptionMock = mockStatic(EncryptionUtil.class)) {
            // конкретные аргументы — безопасно для MockedStatic
            encryptionMock.when(() -> EncryptionUtil.encrypt("1234567890123456"))
                    .thenReturn("encrypted123");
            encryptionMock.when(() -> EncryptionUtil.decrypt("encrypted123"))
                    .thenReturn("1234567890123456");

            CardDTO dto = cardService.createCard(
                    1L,
                    "1234567890123456",
                    LocalDate.now().plusYears(2),
                    new BigDecimal("1000.00"));

            System.out.println("[DEBUG] Created CardDTO: " + dto.getMaskedCardNumber() + ", balance: " + dto.getBalance());

            assertEquals("**** **** **** 3456", dto.getMaskedCardNumber());
            assertEquals(new BigDecimal("1000.00"), dto.getBalance());
            assertEquals("admin", dto.getOwnerUsername());
            assertEquals(CardStatus.ACTIVE, dto.getStatus());
        }
    }

    @Test
    void getUserCards_ShouldReturnCardDTOs() {
        try (MockedStatic<EncryptionUtil> encryptionMock = mockStatic(EncryptionUtil.class)) {
            encryptionMock.when(() -> EncryptionUtil.decrypt("encrypted123"))
                    .thenReturn("1234567890123456");

            List<CardDTO> dtos = cardService.getUserCards(1L);

            dtos.forEach(dto -> System.out.println("[DEBUG] User CardDTO: " + dto.getMaskedCardNumber() + ", balance: " + dto.getBalance()));

            assertEquals(1, dtos.size());
            assertEquals("**** **** **** 3456", dtos.get(0).getMaskedCardNumber());
        }
    }

    @Test
    void updateCardBalance_ShouldUpdateBalance() {
        try (MockedStatic<EncryptionUtil> encryptionMock = mockStatic(EncryptionUtil.class)) {
            encryptionMock.when(() -> EncryptionUtil.decrypt("encrypted123"))
                    .thenReturn("1234567890123456");

            CardDTO dto = cardService.updateCardBalance(1L, new BigDecimal("1500.00"));

            System.out.println("[DEBUG] Updated CardDTO balance: " + dto.getBalance());

            assertEquals(new BigDecimal("1500.00"), dto.getBalance());
        }
    }

    @Test
    void updateCardBalance_ShouldThrowExceptionForNegativeBalance() {
        assertThrows(InsufficientBalanceException.class,
                () -> cardService.updateCardBalance(1L, new BigDecimal("-100")));
        System.out.println("[DEBUG] Negative balance check passed");
    }

    @Test
    void updateCardBalance_ShouldThrowCardNotFoundException() {
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(CardNotFoundException.class,
                () -> cardService.updateCardBalance(2L, new BigDecimal("1000")));
        System.out.println("[DEBUG] Card not found check passed");
    }

    @Test
    void deleteCard_ShouldNotThrowException() {
        assertDoesNotThrow(() -> cardService.deleteCard(1L));
        System.out.println("[DEBUG] Delete card passed");
    }
}
