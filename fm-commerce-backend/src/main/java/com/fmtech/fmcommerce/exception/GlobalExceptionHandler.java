
package com.fmtech.fmcommerce.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.media.Schema;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==========================================
    // Gestion des produits introuvables : HTTP 404
    // ==========================================

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiError> handleProductNotFound(
            ProductNotFoundException ex) {

        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // ==========================================
    // Gestion des erreurs de validation : HTTP 400
    // ==========================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        List<String> messages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                String.join("; ", messages),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(error);
    }

    // ==========================================
    // Gestion des erreurs inattendues : HTTP 500
    // ==========================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(Exception ex) {

        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    // ==========================================
    // Structure commune des réponses d'erreur
    // ==========================================


    // 4. Structure commune des erreurs
    @Schema(
        name = "ApiError",
        description = "Standard error response returned by the FM-Commerce API"
    )
    public record ApiError(
        @Schema(description = "HTTP status code", example = "404")
        int status,

        @Schema(description = "HTTP error description", example = "Not Found")
        String error,

        @Schema(description = "Detailed error message",
                example = "Product not found with id: 999")
        String message,

        @Schema(description = "Date and time when the error occurred",
                example = "2026-11-18T13:30:00",
                type = "string",
                format = "date-time")
        LocalDateTime timestamp
    ) {}
}
