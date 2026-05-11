package com.ecommerce.service;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService
 */
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1));

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testRegisterUser() {
        // Arrange
        User user = new User();
        user.setEmail("newuser@example.com");
        user.setName("New User");
        user.setPassword("password123");
        
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("newuser@example.com");
        savedUser.setName("New User");
        
        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.registerUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUserEmailExists() {
        // Arrange
        User user = new User();
        user.setEmail("existing@example.com");
        
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // Act
        User result = userService.registerUser(user);

        // Assert
        assertNull(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetActiveUsers() {
        // Arrange
        User activeUser = new User();
        activeUser.setActive(true);
        
        when(userRepository.findByActive(true)).thenReturn(Arrays.asList(activeUser));

        // Act
        List<User> result = userService.getActiveUsers();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).getActive());
    }
}
