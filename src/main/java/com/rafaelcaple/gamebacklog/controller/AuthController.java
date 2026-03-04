package com.rafaelcaple.gamebacklog.controller;

import com.rafaelcaple.gamebacklog.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    public record AuthRequest (
            @NotBlank(message = "Username is required")
            @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
            String username,

            @NotBlank(message = "Password is required")
            @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
            String password
    ){}

    @PostMapping("/register")
    public String register (@Valid @RequestBody AuthRequest request) {
        return authService.register(request.username(), request.password());
    }

    @PostMapping("/login")
    public String login (@Valid @RequestBody AuthRequest request) {
        return authService.login(request.username(), request.password());
    }

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        // Erros de validação (@NotBlank, @Size, etc)
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
            List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors()
                    .stream()
                    .map(e -> Map.of("field", e.getField(), "defaultMessage", e.getDefaultMessage()))
                    .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("fieldErrors", fieldErrors));
        }

        // Erros de negócio (username já existe, credenciais inválidas, etc)
        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<Map<String, String>> handleResponseStatus(ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(Map.of("message", ex.getReason()));
        }

        // Qualquer erro inesperado — esconde o detalhe do cliente
        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
            log.error("Unexpected error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Internal server error"));
        }
    }
}
