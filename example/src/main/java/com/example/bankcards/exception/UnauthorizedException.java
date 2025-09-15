package com.example.bankcards.exception;

/**
 * Исключение, выбрасываемое при отсутствии прав доступа.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message){super(message);}

}
