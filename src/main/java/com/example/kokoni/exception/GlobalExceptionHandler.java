package com.example.kokoni.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.kokoni.dto.ErrorInfo;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice 
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfo> handleValidationErrors(MethodArgumentNotValidException e) {
        String details = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorInfo error = new ErrorInfo(400, "Error de validación en los datos", details, LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleEntityNotFound(EntityNotFoundException e) {
        ErrorInfo body = new ErrorInfo(404, "No encontrado: " + e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleUserNotFound(UsernameNotFoundException e) {
        ErrorInfo body = new ErrorInfo(401, "Error de autenticación: " + e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorInfo> handleAccessDenied(AccessDeniedException e) {
        ErrorInfo body = new ErrorInfo(403, "Acceso denegado: " + e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
   
    @ExceptionHandler({TitleException.class})
    public ResponseEntity<ErrorInfo> handleCustomBusinessErrors(RuntimeException e) {
        ErrorInfo body = new ErrorInfo(400, "Error: " + e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleGeneralError(Exception e) {
        ErrorInfo body = new ErrorInfo(500, "Error interno del servidor", e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorInfo> handleRuntimeError(RuntimeException e) {
        ErrorInfo body = new ErrorInfo(400, "Operación fallida", e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
