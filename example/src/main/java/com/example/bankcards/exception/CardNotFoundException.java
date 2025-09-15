package com.example.bankcards.exception;

/**
 * Исключение, выбрасываемое, если карта не найдена.
 */
public class CardNotFoundException extends RuntimeException {

    public CardNotFoundException (String message){ super(message);}
}
