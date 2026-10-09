package dev.luan.digitalbank.exceptions;

import org.springframework.http.HttpStatus;

public class AccountBlockedException extends BusinessException{

    public AccountBlockedException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
