package com.ecommerce.service;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * User Service - Business logic for users/customers
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get user by ID
     */
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    /**
     * Get user by email
     */
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    /**
     * Get active users
     */
    public List<User> getActiveUsers() {
        return userRepository.findByActive(true);
    }

    /**
     * Register new user
     */
    public User registerUser(User user) {
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            return null; // Email already registered
        }

        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setActive(true);
        
        return userRepository.save(user);
    }

    /**
     * Update user profile
     */
    public User updateUser(Long id, User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        
        if (!optionalUser.isPresent()) {
            return null;
        }

        User user = optionalUser.get();
        
        if (userDetails.getName() != null) {
            user.setName(userDetails.getName());
        }
        if (userDetails.getPhone() != null) {
            user.setPhone(userDetails.getPhone());
        }
        if (userDetails.getAddress() != null) {
            user.setAddress(userDetails.getAddress());
        }

        user.setUpdatedAt(new Date());
        return userRepository.save(user);
    }

    /**
     * Delete user
     */
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Activate/Deactivate user
     */
    public User toggleUserStatus(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        
        if (!optionalUser.isPresent()) {
            return null;
        }

        User user = optionalUser.get();
        user.setActive(!user.getActive());
        user.setUpdatedAt(new Date());
        
        return userRepository.save(user);
    }
}
