package com.example.kokoni.entity;

import com.example.kokoni.entity.enums.MediaStatus;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "user_custom_media")
@DiscriminatorValue("CUSTOM") // Valor para la columna media_type en la tabla padre
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCustomMedia extends Media {

    private String customAuthor;
    private Integer customTotalChapters;
    
    @Enumerated(EnumType.STRING)
    private MediaStatus customStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_manga_id")
    private Manga baseManga;
}


