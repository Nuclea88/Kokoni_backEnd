package com.example.kokoni.dto.request;

import com.example.kokoni.entity.enums.UserStatus;

public record UpdateTrackerRequest(
    UserStatus status,
    Integer score,
    String notes
) {

}
