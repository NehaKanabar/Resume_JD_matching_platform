package com.resume.resumematching.exception;

import com.resume.resumematching.common.ApiError;
import com.resume.resumematching.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        List<ApiError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ApiError(
                        err.getField(),
                        err.getDefaultMessage()
                ))
                .toList();

        return ResponseEntity.badRequest().body(
                ApiResponse.failure("Validation failed", errors)
        );
    }

    // Access denied
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDenied(
            AccessDeniedException ex
    ) {
        return ResponseEntity.status(403).body(
                ApiResponse.failure(
                        "Access denied",
                        List.of(new ApiError("authorization", "You do not have permission"))
                )
        );
    }

    // Authentication failure
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthException(
            AuthenticationException ex
    ) {
        return ResponseEntity.status(401).body(
                ApiResponse.failure(
                        "Unauthorized access",
                        List.of(new ApiError("authentication", "Invalid or missing credentials"))
                )
        );
    }

    // Invalid JSON body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidJson(
            HttpMessageNotReadableException ex
    ) {
        return ResponseEntity.badRequest().body(
                ApiResponse.failure(
                        "Invalid request body",
                        List.of(new ApiError("request", "Malformed JSON"))
                )
        );
    }

    // URL / param type mismatch
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex
    ) {
        return ResponseEntity.badRequest().body(
                ApiResponse.failure(
                        "Invalid request parameter",
                        List.of(new ApiError(ex.getName(), "Invalid value"))
                )
        );
    }

    // Business runtime errors
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.badRequest().body(
                ApiResponse.failure(
                        ex.getMessage(),
                        List.of(new ApiError(null, ex.getMessage()))
                )
        );
    }

    // Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(
            ResourceNotFoundException ex
    ) {
        return ResponseEntity.status(404).body(
                ApiResponse.failure(
                        ex.getMessage(),
                        List.of(new ApiError(null, ex.getMessage()))
                )
        );
    }

    // Catch-all (protect Swagger)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception ex) {

        if (ex.getClass().getName().startsWith("org.springdoc")) {
            throw new RuntimeException(ex);
        }

        ex.printStackTrace();

        return ResponseEntity.status(500).body(
                ApiResponse.failure(
                        "Internal server error",
                        List.of(new ApiError(null, "Something went wrong"))
                )
        );
    }
}
