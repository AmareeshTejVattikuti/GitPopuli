package com.redcare.gitpopuli.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.format.DateTimeParseException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomGlobalExceptionHandlerTest {

    private final CustomGlobalExceptionHandler exceptionHandler = new CustomGlobalExceptionHandler();

    @Test
    void testGitHubApiException() {
        GitHubApiException ex = new GitHubApiException("Error calling GitHub API, please try after sometime");

        ResponseEntity<Object> response = exceptionHandler.handleGitHubApiException(ex);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        Map<String, String> body = (Map<String, String>) response.getBody();
        assert body != null;
        System.out.println(body.values());
        assertTrue(body.containsValue("Error calling GitHub API, please try after sometime"));
    }

    @Test
    void testHandleDateTimeParseException() {
        DateTimeParseException ex = new DateTimeParseException("Text 'invalid-date' could not be parsed at index 0", "invalid-date", 0);

        ResponseEntity<Object> response = exceptionHandler.handleDateTimeParseException();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertTrue(body != null && body.get("error").equals("Invalid date format provided. Please use the format yyyy-MM-dd."));
    }
}