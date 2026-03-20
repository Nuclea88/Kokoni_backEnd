package com.example.kokoni.dto.response;

import java.time.LocalDate;

public record ChapterProgressResponse(
    Long id,
    Integer progressUnit,
    Integer subUnit,
    Boolean isSpecial,
    LocalDate readDate,
    String reaction 
) {

}
