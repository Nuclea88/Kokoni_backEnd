package com.example.kokoni.service;

import com.example.kokoni.dto.request.UserCustomMediaDTORequest;
import com.example.kokoni.dto.response.UserCustomMediaDTOResponse;
import com.example.kokoni.entity.UserCustomMedia;

public interface UserCustomMediaService {

    UserCustomMediaDTOResponse createCustom(UserCustomMediaDTORequest request);
    UserCustomMedia findById(Long id);
    UserCustomMediaDTOResponse searchById(Long id);
    UserCustomMediaDTOResponse update(Long id, UserCustomMediaDTORequest request);
    void delete(Long id);
}
