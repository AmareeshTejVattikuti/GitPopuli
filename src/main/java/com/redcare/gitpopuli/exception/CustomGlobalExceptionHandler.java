package com.redcare.gitpopuli.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
class CustomGlobalExceptionHandler {

    private static final String INVALID_DATE_FORMAT = "Invalid date format provided. Please use the format yyyy-MM-dd.";
    private static final String ERROR = "error";
    private static final String ILLEGAL_ACCESS = "You are not allowed to access the required information, please try after sometime";

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Object> handleDateTimeParseException() {
        Map<String, String> errors = new HashMap<>();
        errors.put(ERROR, INVALID_DATE_FORMAT);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Object> handleWebClientResponseException(WebClientResponseException ex) {
        return ResponseEntity.badRequest().body(Map.of(ERROR, ex.getStatusCode() + " - " + ILLEGAL_ACCESS));
    }
}