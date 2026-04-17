package com.example.kokoni.entity;

import com.example.kokoni.entity.enums.MediaStatus;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
//REVISAR, ACTUALIZADO CON PRISAS
@Entity
@Table(name = "mangas")
@DiscriminatorValue("MANGA")
@Getter
@Setter
public class Manga extends Media {

    private Integer totalChapters;
    private Integer totalVolumes;
    private String author;

    @Column(name = "license_owner")
    private String licenseOwner;

    @Enumerated(EnumType.STRING)
    private MediaStatus status;

    private String officialUrl;

    @Column(name = "has_new_update")
    private Boolean hasNewUpdate = false;
    @Column(name = "last_update_at")
    private java.time.LocalDateTime lastUpdateAt;
    @Column(name = "update_source")
    private String updateSource;
}
