package com.example.user.service;

import com.example.user.model.User;
import com.example.user.publisher.UserSyncPublisher;
import com.example.user.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserSyncPublisher syncPublisher;

    public UserService(UserRepository userRepository, UserSyncPublisher syncPublisher) {
        this.userRepository = userRepository;
        this.syncPublisher = syncPublisher;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        User savedUser = userRepository.save(user);

        syncPublisher.publishUserCreated(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
        return savedUser;
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setEmail(userDetails.getEmail());
        user.setFullName(userDetails.getFullName());

        User updatedUser = userRepository.save(user);

        syncPublisher.publishUserUpdated(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail());
        return updatedUser;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);

        syncPublisher.publishUserDeleted(id);
    }
}