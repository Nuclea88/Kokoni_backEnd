package com.example.kokoni.controller;

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
import com.example.kokoni.service.UserCustomMediaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/custom-media")
@RequiredArgsConstructor
public class UserCustomMediaController {

    private final UserCustomMediaService customMediaService;

    @PostMapping
    public ResponseEntity<UserCustomMediaDTOResponse> create(
           @Valid @RequestBody UserCustomMediaDTORequest request) {
        
        UserCustomMediaDTOResponse newCustomMedia = customMediaService.createCustom(request);
        return new ResponseEntity<>(newCustomMedia, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserCustomMediaDTOResponse> searchById(@PathVariable Long id){
        return new ResponseEntity<>(customMediaService.searchById(id),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserCustomMediaDTOResponse> update(
            @PathVariable Long id, 
            @Valid @RequestBody UserCustomMediaDTORequest request) {
        return ResponseEntity.ok(customMediaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customMediaService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}