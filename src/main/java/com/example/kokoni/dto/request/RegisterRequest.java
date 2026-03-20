package com.example.kokoni.dto.request;

public record RegisterRequest(
    String username,
    String email,
    String password
) {}
