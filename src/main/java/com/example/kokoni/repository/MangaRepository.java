package com.example.kokoni.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.kokoni.entity.Manga;

@Repository
public interface MangaRepository extends JpaRepository<Manga, Long> {
    Optional<Manga> findByExternalId(String externalId);
}