package com.example.kokoni.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_chapter_progress")
@Getter @Setter
public class UserChapterProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tracker_id")
    private UserMediaTracker tracker;

    private Integer chapterNumber;
    private Integer volumeNumber; // O "Season"
    
    private LocalDate readDate;
    
    private String reaction; // Guardamos el nombre del emoji: "FIRE", "CRY", "LOVE"

    @Column(name = "is_special")
    private Boolean isSpecial = false; // Para capítulos .5 o extras
}