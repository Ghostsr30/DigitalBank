package dev.luan.digitalbank.exceptions;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException{

    private HttpStatus status;

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
