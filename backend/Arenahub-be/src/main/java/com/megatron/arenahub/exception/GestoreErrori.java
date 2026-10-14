package com.megatron.arenahub.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

// Tutti gli errori escono nel formato { "errore": "..." }
@RestControllerAdvice
public class GestoreErrori {

    private ResponseEntity<Map<String, String>> risposta(HttpStatus stato, String messaggio) {
        return ResponseEntity.status(stato).body(Map.of("errore", messaggio));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, String>> gestisciApi(ApiException e) {
        return risposta(e.getStato(), e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> bodyNonValido(HttpMessageNotReadableException e) {
        return risposta(HttpStatus.BAD_REQUEST, "Body mancante o non valido");
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<Map<String, String>> parametroNonValido(Exception e) {
        return risposta(HttpStatus.BAD_REQUEST, "Parametro mancante o non valido");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> conflitto(DataIntegrityViolationException e) {
        return risposta(HttpStatus.CONFLICT, "Operazione non consentita: violazione di un vincolo del database");
    }
}
