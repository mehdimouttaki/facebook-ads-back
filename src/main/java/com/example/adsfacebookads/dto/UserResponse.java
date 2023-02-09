package com.example.adsfacebookads.dto;

import com.example.adsfacebookads.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id ;
    private String firstName;
    private String lastName;
    private String email ;
    private String username;
    private String password;
    private String role;
}
