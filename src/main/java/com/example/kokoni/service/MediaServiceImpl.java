package com.example.kokoni.service;

import org.springframework.stereotype.Service;

import com.example.kokoni.entity.Media;
import com.example.kokoni.repository.MediaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService{

    private final MediaRepository mediaRepository;

    @Override
    public Media findById(Long id) {
        return mediaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Contenido Media no encontrado"));
    }
}
