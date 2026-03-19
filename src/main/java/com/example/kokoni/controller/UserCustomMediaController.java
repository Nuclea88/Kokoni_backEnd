package com.example.kokoni.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kokoni.entity.User;
import com.example.kokoni.entity.UserCustomMedia;
import com.example.kokoni.service.UserCustomMediaService;
import com.example.kokoni.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/custom-media")
@RequiredArgsConstructor
public class UserCustomMediaController {

    private final UserCustomMediaService customMediaService;
    private final UserService userService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<UserCustomMedia> create(
            @PathVariable Long userId, 
            @RequestBody UserCustomMedia custom) {
        
        User creator = userService.findById(userId);
        UserCustomMedia newCustomMedia = customMediaService.createCustom(custom, creator);
        return new ResponseEntity<>(newCustomMedia, HttpStatus.CREATED);
    }
}