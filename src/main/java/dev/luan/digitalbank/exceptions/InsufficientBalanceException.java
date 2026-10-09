package dev.luan.digitalbank.exceptions;

import org.springframework.http.HttpStatus;

public class InsufficientBalanceException extends BusinessException{

    public InsufficientBalanceException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
