package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void add_ShouldSaveUser() {
        User user = new User();
        user.setName("John");
        userService.add(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getById_ShouldReturnUser_WhenFound() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.getById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.getById(99L));
    }

    @Test
    void getUsers_ShouldReturnList() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(new User(), new User()));
        List<User> result = userService.getUsers();
        assertEquals(2, result.size());
    }

    @Test
    void update_ShouldUpdateFieldsAndSave() {
        Long id = 1L;
        User existingUser = new User();
        existingUser.setId(id);
        existingUser.setName("Old Name");
        existingUser.setLocation("Old Loc");
        existingUser.setEmail("old@test.com");
        User updatedData = new User();
        updatedData.setId(id);
        updatedData.setName("New Name");
        updatedData.setLocation("New Loc");
        updatedData.setEmail("new@test.com");
        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        userService.update(updatedData);
        verify(userRepository).save(existingUser);
        assertEquals("New Name", existingUser.getName());
        assertEquals("new@test.com", existingUser.getEmail());
    }

    @Test
    void deleteById_ShouldDelete_WhenExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        userService.deleteById(1L);
        verify(userRepository).deleteById(1L);
    }
}