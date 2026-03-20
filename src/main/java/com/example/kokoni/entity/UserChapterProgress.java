package com.example.kokoni.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_chapter_progress", uniqueConstraints = {@UniqueConstraint(columnNames = {"tracker_id", "progress_unit"})})
@Getter @Setter
public class UserChapterProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tracker_id")
    private UserMediaTracker tracker;

    @Column(name = "progress_unit", nullable = false)
    private Integer progressUnit;

    @Column(name ="sub_unit")
    private Integer subUnit; 
    
    private LocalDate readDate;
    
    private String reaction; // Guardamos el nombre del emoji: "FIRE", "CRY", "LOVE"

    @Column(name = "is_special")
    private Boolean isSpecial = false; // Para capítulos .5 o extras

    @Column(updatable = false)
    private LocalDateTime createdAt;
}