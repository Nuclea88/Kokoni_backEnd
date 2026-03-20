package com.example.kokoni.dto.request;

public record AddChapterProgressRequest(
    Integer progressUnit, 
    Integer subUnit,    
    Boolean isSpecial,
    String reaction  
) {

}
