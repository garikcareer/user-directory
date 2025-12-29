package com.example.service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create
    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    // Read
    @Override
    public User getById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Company not found"));
    }

    @Override
    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    // Update
    @Override
    public void updateUser(Long userId, User user) {
        User existingUser = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("User not found with id: " + userId));
        existingUser.setName(user.getName());
        existingUser.setLocation(user.getLocation());
        userRepository.save(existingUser);
    }

    // Delete
    @Override
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }
}
