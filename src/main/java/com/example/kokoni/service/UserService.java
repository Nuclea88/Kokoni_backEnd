package com.example.kokoni.service;

import com.example.kokoni.entity.User;

public interface UserService {

    User findById(Long id);
    User registerUser(User user);
    void updateUser(Long id, User userDetails);
    void deleteUser(Long id);

}
