package com.example.kokoni.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kokoni.dto.request.UpdateUserRequest;
import com.example.kokoni.dto.response.UserProfileResponse;
import com.example.kokoni.entity.User;
import com.example.kokoni.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
    User user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

     @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile() {
         
        UserProfileResponse profileData = userService.getUserProfile();
        return new ResponseEntity<>(profileData, HttpStatus.OK);
    }

    @PutMapping("/me")
    public ResponseEntity<HttpStatus> updateUSer(@Valid @RequestBody UpdateUserRequest request) {
        userService.updateUser(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/me")
    public ResponseEntity<HttpStatus> deleteUser() {
        userService.deleteUser();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
}