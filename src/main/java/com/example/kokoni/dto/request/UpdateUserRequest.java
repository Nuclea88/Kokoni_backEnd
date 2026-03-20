package com.example.kokoni.dto.request;

public record UpdateUserRequest(

    String username,
    String email,
    String avatarUrl,
    String password 
) {

}
