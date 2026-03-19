package com.example.kokoni.service;

import com.example.kokoni.dto.request.UserCustomMediaDTORequest;
import com.example.kokoni.dto.response.UserCustomMediaDTOResponse;
import com.example.kokoni.entity.User;
import com.example.kokoni.entity.UserCustomMedia;

public interface UserCustomMediaService {

    UserCustomMedia createCustom(UserCustomMediaDTORequest request, Long userId);
    UserCustomMedia findById(Long id);
    UserCustomMediaDTOResponse searchById(Long id);
    UserCustomMedia update(Long id, UserCustomMediaDTORequest request);
    void delete(Long id);
}
