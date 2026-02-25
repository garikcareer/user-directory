package com.example.service;

import com.example.model.User;
import org.springframework.data.domain.Page;
import java.util.List;

public interface UserService {
    void add(User user);
    User getById(Long id);
    List<User> getUsers();
    Page<User> getUsersPage(int page, int size, String sortField, String sortDir);
    void update(User user);
    void deleteById(Long id);
}
