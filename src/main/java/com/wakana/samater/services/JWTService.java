package com.wakana.samater.services;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


public interface JWTService {
    String extractUserName(String token);
    
     String generateToken(UserDetails userDetails);
     String generateRefreshToken(Map<String, Object> extraClaims,UserDetails userDetails);
     
    boolean isTokenValid(String token , UserDetails userDetails);
}
