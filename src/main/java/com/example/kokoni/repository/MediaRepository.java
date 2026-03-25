package com.example.kokoni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kokoni.entity.Media;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
}