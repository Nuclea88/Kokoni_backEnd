package com.example.kokoni.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "media_update_logs")
@Getter @Setter
public class MediaUpdateLog {


    //REVISAR, HECHO CON PRISAS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "original_title", nullable = false)
    private String originalTitle;
    @Column(name = "extracted_chapter")
    private String extractedChapter;
    @Column(name = "source")
    private String source;
    @Column(name = "media_id")
    private Long mediaId; 
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}