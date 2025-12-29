package com.example.service;

import com.example.entity.User;
import java.util.List;

public interface UserService {
    void save(User user);
    User getById(Long userId);
    List<User> getUsers();
    void updateUser(Long userId, User user);
    void deleteById(Long userId);
}
