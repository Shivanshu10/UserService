package com.shivanshu.secexample.jwt;

import java.security.Key;

import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtUtils {
    public String getJwtFromHeader(HttpServletRequest request);

    public String generateTokenFromUsername(UserDetails userDetails);
    
    public String getUsernameFromToken(String token);

    public Key key();

    public boolean validateToken(String token);
}
