package dev.luan.digitalbank.exceptions;

import org.springframework.http.HttpStatus;

public class AcessDeniedException extends BusinessException {

    public AcessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
