package com.example.kokoni.entity;

import java.time.LocalDate;

import com.example.kokoni.entity.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "user_media_tracker")
@Getter @Setter
public class UserMediaTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    private Media media;

    private Integer score; // Tu nota personal (1-10)

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus; // LEYENDO, PENDIENTE, TERMINADO...

    private LocalDate startDate;
    private LocalDate finishDate;

    @Column(columnDefinition = "TEXT")
    private String notes;
}