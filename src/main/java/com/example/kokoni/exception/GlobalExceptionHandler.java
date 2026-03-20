package com.example.kokoni.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.kokoni.dto.ErrorInfo;

import jakarta.persistence.EntityNotFoundException;


@ControllerAdvice
public class GlobalExceptionHandler {

//     @ExceptionHandler(UnauthorizedException.class)
//     public ResponseEntity<ErrorInfo> handleUnauthorized(UnauthorizedException ex) {
//         ErrorInfo error = new ErrorInfo(401,"unauthorized: "+ ex.getMessage());
//         return new ResponseEntity<>(error,HttpStatus.UNAUTHORIZED);
//     }
    
//     @ExceptionHandler(MethodArgumentNotValidException.class)
//     public ResponseEntity<ErrorInfo> handleValidationErrors(MethodArgumentNotValidException e) {
        
//         String details = e.getBindingResult().getFieldErrors().stream()
//                 .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                 .collect(Collectors.joining(", "));

//         ErrorInfo error = new ErrorInfo(400, "Error in data validation", details, LocalDateTime.now());
//         return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//     }
//     @ExceptionHandler(NoResourceFoundException.class)
//     public ResponseEntity<ErrorInfo> notFoundError(NoResourceFoundException e) {
//         ErrorInfo body = new ErrorInfo(404, "Not found: " + e.getMessage());
//         return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//     }

//     @ExceptionHandler(FileException.class)
//     public ResponseEntity<ErrorInfo> fileError (FileException e){
//         ErrorInfo body = new ErrorInfo(400, e.getMessage());
//     return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//     }
    @ExceptionHandler(TitleException.class)
    public ResponseEntity<ErrorInfo> TitleError (TitleException e){
        ErrorInfo body = new ErrorInfo(400, "Error en la respuesta: " + e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorInfo> generalError(RuntimeException e) {
        ErrorInfo body = new ErrorInfo(500, "Internal error: " + e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

     @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorInfo> EntityNotFoundError(EntityNotFoundException e) {
        ErrorInfo body = new ErrorInfo(404, "not found: " + e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorInfo> UsernameNotFoundError(UsernameNotFoundException e) {
        ErrorInfo body = new ErrorInfo(403, "bad credentials: " + e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
}
