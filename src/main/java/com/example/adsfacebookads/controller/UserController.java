package com.example.adsfacebookads.controller;


import com.example.adsfacebookads.dto.UserRequest;
import com.example.adsfacebookads.dto.UserResponse;
import com.example.adsfacebookads.entity.Role;
import com.example.adsfacebookads.entity.RoleRepository;
import com.example.adsfacebookads.entity.User;
import com.example.adsfacebookads.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder ;
    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init(){
        boolean existed= userService.existByUsername("admin");
        if (!existed){
            User user =new User();
            user.setFirstName("ADMIN");
            user.setLastName("Admin");
            user.setEmail("admin@admin.com");
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("123456"));
            userService.save(user);
        }

    }

    @PostConstruct
    public void role(){


            Role role =new Role();
            role.setName("ADMIN");
            roleRepository.save(role);

    }
    @PostConstruct
    public void roleUser(){

            Role role =new Role();
            role.setName("USER");
            roleRepository.save(role);

    }
    @GetMapping("/users") //ADMIN and EDITOR
    @PreAuthorize("hasAuthority('ADMIN')")
    List<User> findAllUsers(){
        return userService.findAllUsers();
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    ResponseEntity<User> findByUserId(@PathVariable("userId") @Min(1) int userId){
        return userService.findByUserId(userId);
    }

    @PostMapping("/users") //ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    UserResponse createUser(@RequestBody @Valid UserRequest request) throws Exception {
        return userService.createUser(request);
    }

    @PutMapping("/users/{userId}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    ResponseEntity<User> updateUser(@RequestBody @Valid User user, @PathVariable("userId") @Min(1) int userId){
        return userService.updateUser(user, userId);
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<String> deleteUserById(@PathVariable @Min(1) int userId){
        return userService.deleteUserById(userId);
    }

}
