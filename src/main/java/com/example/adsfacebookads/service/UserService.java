package com.example.adsfacebookads.service;


import com.example.adsfacebookads.dto.UserRequest;
import com.example.adsfacebookads.dto.UserResponse;
import com.example.adsfacebookads.entity.Role;
import com.example.adsfacebookads.entity.User;
import com.example.adsfacebookads.exception.ResourceNotFoundException;
import com.example.adsfacebookads.mapper.USerResponseMapper;
import com.example.adsfacebookads.mapper.UserRequestMapper;
import com.example.adsfacebookads.repository.RoleRepository;
import com.example.adsfacebookads.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRequestMapper userRequestMapper;
    @Autowired
    private USerResponseMapper uSerResponseMapper;

    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User save(User user) {

        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<User> findByUserId(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User Id : %d is not found", userId)));
        return ResponseEntity.ok().body(user);
    }

    public UserResponse createUser(UserRequest request) throws Exception {
        User user = userRequestMapper.targetToSource(request);
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setRoles(new HashSet<>());
        for (Long roles : request.getRoles()) {
            Role role = roleRepository.findById(roles).orElseThrow(() -> new ResourceNotFoundException("Role Id Not Found "));
            user.getRoles().add(role);
        }
        return uSerResponseMapper.sourceToTarget(userRepository.save(user));
    }

    public ResponseEntity<User> updateUser(User newUser, int userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User Id : %d is not found", userId)));
        newUser.setUserId(userId);
        return ResponseEntity.ok(userRepository.save(newUser));
    }

    public ResponseEntity<String> deleteUserById(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User Id : %d is not found", userId)));
        userRepository.deleteById(user.getUserId());
        return ResponseEntity.ok().body("User id: " + userId + " is deleted.");
    }
}