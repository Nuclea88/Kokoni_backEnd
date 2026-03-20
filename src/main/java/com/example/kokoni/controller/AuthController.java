package com.example.kokoni.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.kokoni.dto.request.RegisterRequest;
import com.example.kokoni.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/register") 
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        userService.registerUser(request);
        return new ResponseEntity<>("Usuario registrado con éxito. Ya puedes iniciar sesión.", HttpStatus.CREATED);
    }
}