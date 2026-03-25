package com.example.kokoni.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.kokoni.dto.request.RegisterRequest;
import com.example.kokoni.dto.request.UpdateUserRequest;
import com.example.kokoni.dto.response.UserProfileResponse;
import com.example.kokoni.entity.User;

public interface UserService extends UserDetailsService{

    User findById(Long id);

    User registerUser(RegisterRequest request);

    UserProfileResponse getUserProfile();

    void updateUser(UpdateUserRequest request);

    void deleteUser();
}
