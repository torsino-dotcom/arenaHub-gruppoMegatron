package com.megatron.arenahub.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final HttpStatus stato;

    public ApiException(HttpStatus stato, String messaggio) {
        super(messaggio);
        this.stato = stato;
    }

    public HttpStatus getStato() {
        return stato;
    }
}
