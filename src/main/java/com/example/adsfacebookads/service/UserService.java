package com.example.adsfacebookads.service;


import com.example.adsfacebookads.entity.Role;
import com.example.adsfacebookads.entity.RoleRepository;
import com.example.adsfacebookads.entity.User;
import com.example.adsfacebookads.exception.ResourceNotFoundException;
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
                .orElseThrow(()-> new ResourceNotFoundException(String.format("User Id : %d is not found", userId)));
        return ResponseEntity.ok().body(user);
    }

    public ResponseEntity<User> createUser(User user) {
        String encPwd = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encPwd);
        return ResponseEntity.ok(userRepository.save(user));
    }

    public ResponseEntity<User> updateUser(User userData, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException(String.format("User Id : %d is not found", userId)));
        user.setUserId(userId);
        return ResponseEntity.ok(userRepository.save(user));
    }

    public ResponseEntity<String> deleteUserById(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException(String.format("User Id : %d is not found", userId)));
        userRepository.deleteById(user.getUserId());
        return ResponseEntity.ok().body("User id: "+userId+" is deleted.");
    }
}