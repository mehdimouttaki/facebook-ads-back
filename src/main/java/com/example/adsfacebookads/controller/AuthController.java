package com.example.adsfacebookads.controller;


import com.example.adsfacebookads.dto.JwtResponse;
import com.example.adsfacebookads.dto.LoginRequest;
import com.example.adsfacebookads.jwt.JwtUtils;
import com.example.adsfacebookads.service.MyCustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/token")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        MyCustomUserDetails userDetails = (MyCustomUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities()
                .stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUsername(),userDetails.getUser().getFirstName(),userDetails.getUser().getLastName(),
                roles,userDetails.getUser().getUserId()));
        }catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }
}

