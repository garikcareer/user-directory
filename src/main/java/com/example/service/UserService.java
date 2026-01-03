package com.example.service;

import com.example.model.User;
import java.util.List;

public interface UserService {
    void add(User user);
    User getById(Long id);
    List<User> getUsers();
    void update(User user);
    void deleteById(Long id);
}
