package com.example.kokoni.dto.request;

import com.example.kokoni.entity.enums.UserStatus;

public record AddTrackerRequest(
    String externalId,    
    UserStatus status,    
    Integer score 
) {

}
