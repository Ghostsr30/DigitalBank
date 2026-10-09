package dev.luan.digitalbank.exceptions;

import org.springframework.http.HttpStatus;

public class AccountHasBalanceException extends BusinessException{

    public AccountHasBalanceException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
