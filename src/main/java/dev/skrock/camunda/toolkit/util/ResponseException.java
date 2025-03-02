package dev.skrock.camunda.toolkit.util;

import org.springframework.http.ResponseEntity;

public class ResponseException extends Exception {
    private final transient ResponseEntity<?> response;

    public ResponseException(String message, ResponseEntity<?> response) {
        super(message);
        this.response = response;
    }

    @Override
    public String getMessage() {
        return """
            %s
            
            Response: %s
            """.formatted(super.getMessage(), response);
    }
}