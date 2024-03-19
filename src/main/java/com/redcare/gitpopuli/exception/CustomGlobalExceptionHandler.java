package com.redcare.gitpopuli.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.http.ResponseEntity;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
class CustomGlobalExceptionHandler {

    private static final String INVALID_DATE_FORMAT = "Invalid date format provided. Please use the format yyyy-MM-dd.";
    private static final String ERROR = "error";

    @ExceptionHandler(GitHubApiException.class)
    public ResponseEntity<Object> handleGitHubApiException(GitHubApiException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of(ERROR, ex.getMessage()));
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Object> handleDateTimeParseException() {
        Map<String, String> errors = new HashMap<>();
        errors.put(ERROR, INVALID_DATE_FORMAT);
        return ResponseEntity.badRequest().body(errors);
    }
}