package com.example.bankcards.exception;

/**
 * Исключение, выбрасываемое, если баланс недостаточен для операции.
 */
public class InsufficientBalanceException extends RuntimeException{

    public InsufficientBalanceException (String message){ super(message);}

}
