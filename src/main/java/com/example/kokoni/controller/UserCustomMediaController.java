package com.example.kokoni.controller;

import com.example.kokoni.service.UserCustomMediaServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kokoni.dto.request.UserCustomMediaDTORequest;
import com.example.kokoni.dto.response.UserCustomMediaDTOResponse;
import com.example.kokoni.entity.User;
import com.example.kokoni.entity.UserCustomMedia;
import com.example.kokoni.mapper.UserCustomMediaMapper;
import com.example.kokoni.service.UserCustomMediaService;
import com.example.kokoni.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/custom-media")
@RequiredArgsConstructor
public class UserCustomMediaController {

    private final UserCustomMediaService customMediaService;
    private final UserService userService;
    private final UserCustomMediaMapper customMediaMapper;

    @PostMapping("/user/{userId}")
    public ResponseEntity<UserCustomMediaDTOResponse> create(
            @PathVariable Long userId, 
            @RequestBody UserCustomMediaDTORequest custom) {
        
        UserCustomMedia newCustomMedia = customMediaService.createCustom(custom, userId);
        UserCustomMediaDTOResponse newMediaDTO = customMediaMapper.toResponse(newCustomMedia);
        return new ResponseEntity<>(newMediaDTO, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserCustomMediaDTOResponse> searchById(@PathVariable Long id){
        return new ResponseEntity<>(customMediaService.searchById(id),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserCustomMediaDTOResponse> update(
            @PathVariable Long id, 
            @RequestBody UserCustomMediaDTORequest request) {
        return ResponseEntity.ok(customMediaMapper.toResponse(customMediaService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customMediaService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}