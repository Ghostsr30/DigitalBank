package dev.luan.digitalbank.exceptions;

import org.springframework.http.HttpStatus;

public class AccountClosedException extends BusinessException{

    public AccountClosedException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
