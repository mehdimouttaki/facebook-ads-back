package com.example.adsfacebookads.controller;


import com.example.adsfacebookads.dto.UpdatePasswordRequest;
import com.example.adsfacebookads.dto.UserDTO;
import com.example.adsfacebookads.dto.UserRequest;
import com.example.adsfacebookads.dto.UserResponse;
import com.example.adsfacebookads.entity.Role;
import com.example.adsfacebookads.entity.User;
import com.example.adsfacebookads.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        boolean existed = userService.existByUsername("admin");
        if (!existed) {
            Set<Role> roles = new HashSet<>();
            roles.add(new Role("ADMIN"));
            roles.add(new Role("USER"));
            User user = new User("ADMIN", "Admin", "admin@admin.com", "admin", passwordEncoder.encode("123456"), roles);

            userService.save(user);
        }

    }

    @GetMapping("/users/{pageNumber}/{pageSize}") //ADMIN and EDITOR
    @PreAuthorize("hasAuthority('ADMIN')")
    List<User> findAllUsers(@PathVariable Integer pageNumber ,
                                    @PathVariable Integer pageSize) throws Exception {

        return userService.findAllUsers(pageNumber,pageSize);
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    ResponseEntity<User> findByUserId(@PathVariable("userId") @Min(1) Long userId) {
        return userService.findByUserId(userId);
    }

    @PostMapping("/user") //ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest request) throws Exception {
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.OK);
    }

    @PutMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserRequest userRequest, @PathVariable("userId")  Long userId) throws Exception {
        return new ResponseEntity<>(userService.updateUser(userRequest, userId), HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.deleteUserById(userId), HttpStatus.OK);
    }

    @PutMapping("/user/updatePassword/{userId}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    ResponseEntity<UserDTO> updatePassword(@PathVariable Long userId,
                                           @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) throws Exception {
    return new ResponseEntity<>(userService.updatePassword(userId,updatePasswordRequest),HttpStatus.OK);
    }

}
