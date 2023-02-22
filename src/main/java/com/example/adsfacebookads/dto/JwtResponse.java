package com.example.adsfacebookads.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String username;
    private String firstName;
    private String lastName;
    private List<String> roles;
    private Long userId;
}
