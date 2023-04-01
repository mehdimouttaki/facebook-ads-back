package com.example.adsfacebookads.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email ;
    private String username;
    private String password;
    private List<Long> roles;
    private Long adminId;
}
