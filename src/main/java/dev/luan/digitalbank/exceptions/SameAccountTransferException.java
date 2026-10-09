package dev.luan.digitalbank.exceptions;

import org.springframework.http.HttpStatus;

public class SameAccountTransferException extends  BusinessException {

    public SameAccountTransferException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
