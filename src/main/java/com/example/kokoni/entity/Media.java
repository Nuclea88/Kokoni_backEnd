package com.example.kokoni.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "media")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "media_type", discriminatorType = DiscriminatorType.STRING)
@Getter @Setter
public class Media {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "external_id", unique = true)
    private String externalId;

    @Column(nullable = false)
    private String provider; 

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Getter @Setter
    private List<MediaTitle> titles = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "media_genres", joinColumns = @JoinColumn(name = "media_id"))
    @Column(name = "genre")
    private Set<String> genres = new HashSet<>();

    private Double averageScore;
    private Integer popularityCount;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt;

    public void addTitle(MediaTitle title) {
        titles.add(title);
        title.setMedia(this);
    }
}
