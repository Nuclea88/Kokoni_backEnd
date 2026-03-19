package com.example.kokoni.service;

import com.example.kokoni.entity.User;
import com.example.kokoni.entity.UserCustomMedia;

public interface UserCustomMediaService {

    UserCustomMedia createCustom(UserCustomMedia custom, User creator);
    UserCustomMedia findById(Long id);
}
