package com.resume.resumematching.exception;

import com.resume.resumematching.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err ->
                        errors.put(err.getField(), err.getDefaultMessage())
                );

        return ResponseEntity.badRequest().body(
                ApiResponse.failure("Validation failed", errors)
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDenied(
            AccessDeniedException ex
    ) {
        return ResponseEntity.status(403).body(
                ApiResponse.failure("Access denied", null)
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthException(
            AuthenticationException ex
    ) {
        return ResponseEntity.status(401).body(
                ApiResponse.failure("Unauthorized access", null)
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidJson(
            HttpMessageNotReadableException ex
    ) {
        return ResponseEntity.badRequest().body(
                ApiResponse.failure("Invalid request body", null)
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex
    ) {
        return ResponseEntity.badRequest().body(
                ApiResponse.failure("Invalid request parameter", null)
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.badRequest().body(
                ApiResponse.failure(ex.getMessage(), null)
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(
            ResourceNotFoundException ex
    ) {
        return ResponseEntity.status(404).body(
                ApiResponse.failure(ex.getMessage(), null)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleAll(Exception ex) {
        return ResponseEntity.status(500).body(
                ApiResponse.failure("Internal server error", null)
        );
    }

}
