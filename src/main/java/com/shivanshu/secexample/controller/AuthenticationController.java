package com.shivanshu.secexample.controller;

import org.springframework.web.bind.annotation.RestController;

import com.shivanshu.secexample.dto.BaseDTO;
import com.shivanshu.secexample.dto.LoginRequest;
import com.shivanshu.secexample.dto.LoginResponse;
import com.shivanshu.secexample.jwt.JwtUtils;
import com.shivanshu.secexample.security.CustomUserDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AuthenticationController {

    @Autowired
    private CustomUserDetailService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<BaseDTO<LoginResponse>> authenticateUser(@RequestBody LoginRequest loginRequest) {
        UserDetails userDetails;
        try
        {
                userDetails = userService.loadUserByUsername(loginRequest.getUsername());
        }
        catch (UsernameNotFoundException e) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(BaseDTO.<LoginResponse>builder()
                                .statusCode(HttpStatus.UNAUTHORIZED)
                                .message("Invalid username or password")
                                .data(null)
                                .build());
        }
        // generate token
        String jwt = jwtUtils.generateTokenFromUsername(userDetails);

        LoginResponse loginResponse = LoginResponse.builder()
                .username(userDetails.getUsername())
                .jwtToken(jwt)
                .roles(userDetails.getAuthorities().stream()
                        .map(auth -> auth.getAuthority())
                        .toList())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseDTO.<LoginResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .message("User authenticated successfully")
                        .data(loginResponse)
                        .build());
    }
}
