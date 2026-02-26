package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(User user) {
        userRepository.save(user);
    }

    @Override
    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> searchUsersPage(String search, int page, int size, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        if (search == null || search.isBlank()) {
            return userRepository.findAll(pageable);
        }
        return userRepository.searchUsers(search.trim(), pageable);
    }

    @Override
    public void update(User user) {
        User existing = getById(user.getId());
        existing.setName(user.getName());
        existing.setLocation(user.getLocation());
        existing.setEmail(user.getEmail());
        existing.setPhone(user.getPhone());
        userRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}