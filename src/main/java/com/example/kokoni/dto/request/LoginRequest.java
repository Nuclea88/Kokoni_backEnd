package com.example.kokoni.dto.request;

public record LoginRequest(
    String usernameOrEmail,
    String password
) {}
