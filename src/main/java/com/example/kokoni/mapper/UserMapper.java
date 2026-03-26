package com.example.kokoni.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.kokoni.dto.request.RegisterRequest;
import com.example.kokoni.dto.response.UserProfileResponse;
import com.example.kokoni.entity.User;

@Mapper(componentModel ="spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
   
    UserProfileResponse toProfileResponse(User user, Integer level, String rankName, Integer totalChaptersRead, Integer streakDays, Integer timeReadHours);

    User toEntity(RegisterRequest request);
}
