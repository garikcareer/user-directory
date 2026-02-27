package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    // --- add ---

    @Test
    void add_ShouldSaveUser() {
        User user = new User();
        user.setName("John");
        userService.add(user);
        verify(userRepository, times(1)).save(user);
    }

    // --- getById ---

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

    // --- getUsers ---

    @Test
    void getUsers_ShouldReturnList() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(new User(), new User()));
        List<User> result = userService.getUsers();
        assertEquals(2, result.size());
    }

    // --- searchUsersPage: blank / null search delegates to findAll(Pageable) ---

    @Test
    void searchUsersPage_WithBlankSearch_ShouldCallFindAll() {
        Page<User> page = new PageImpl<>(List.of(new User(), new User()));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<User> result = userService.searchUsersPage("", 0, 10, "name", "asc");

        assertEquals(2, result.getContent().size());
        verify(userRepository).findAll(any(Pageable.class));
        verify(userRepository, never()).searchUsers(any(), any());
    }

    @Test
    void searchUsersPage_WithNullSearch_ShouldCallFindAll() {
        Page<User> page = new PageImpl<>(List.of(new User()));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<User> result = userService.searchUsersPage(null, 0, 10, "name", "asc");

        assertEquals(1, result.getContent().size());
        verify(userRepository).findAll(any(Pageable.class));
        verify(userRepository, never()).searchUsers(any(), any());
    }

    // --- searchUsersPage: non-blank term delegates to searchUsers query ---

    @Test
    void searchUsersPage_WithSearchTerm_ShouldCallSearchUsers() {
        User matched = new User("Alice Johnson", "New York, NY", "alice@example.com");
        Page<User> page = new PageImpl<>(List.of(matched));
        when(userRepository.searchUsers(eq("Alice"), any(Pageable.class))).thenReturn(page);

        Page<User> result = userService.searchUsersPage("Alice", 0, 10, "name", "asc");

        assertEquals(1, result.getContent().size());
        assertEquals("Alice Johnson", result.getContent().get(0).getName());
        verify(userRepository).searchUsers(eq("Alice"), any(Pageable.class));
        verify(userRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void searchUsersPage_FirstResultMatchesSearchTerm() {
        User alice = new User("Alice Johnson", "New York, NY", "alice@example.com");
        User adam  = new User("Adam Smith",    "Boston, MA",  "adam@example.com");
        Page<User> page = new PageImpl<>(List.of(alice, adam));
        when(userRepository.searchUsers(eq("a"), any(Pageable.class))).thenReturn(page);

        Page<User> result = userService.searchUsersPage("a", 0, 10, "name", "asc");

        // First row in the page must be the first match returned by the repository
        assertEquals("Alice Johnson", result.getContent().get(0).getName());
    }

    @Test
    void searchUsersPage_WithDescendingSort_ShouldReturnPagedResults() {
        Page<User> page = new PageImpl<>(List.of(new User()));
        when(userRepository.searchUsers(eq("john"), any(Pageable.class))).thenReturn(page);

        Page<User> result = userService.searchUsersPage("john", 0, 10, "name", "desc");

        assertNotNull(result);
        verify(userRepository).searchUsers(eq("john"), any(Pageable.class));
    }

    // --- update ---

    @Test
    void update_ShouldUpdateFieldsAndSave() {
        Long id = 1L;
        User existingUser = new User();
        existingUser.setId(id);
        existingUser.setName("Old Name");
        existingUser.setLocation("Old Loc");
        existingUser.setEmail("old@test.com");
        existingUser.setPhone("+1 555-0000");

        User updatedData = new User();
        updatedData.setId(id);
        updatedData.setName("New Name");
        updatedData.setLocation("New Loc");
        updatedData.setEmail("new@test.com");
        updatedData.setPhone("+1 555-9999");

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        userService.update(updatedData);

        verify(userRepository).save(existingUser);
        assertEquals("New Name", existingUser.getName());
        assertEquals("new@test.com", existingUser.getEmail());
        assertEquals("+1 555-9999", existingUser.getPhone());
    }

    // --- deleteById ---

    @Test
    void deleteById_ShouldDelete_WhenExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        userService.deleteById(1L);
        verify(userRepository).deleteById(1L);
    }
}