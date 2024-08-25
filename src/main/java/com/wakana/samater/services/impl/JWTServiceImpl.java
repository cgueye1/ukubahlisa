package com.wakana.samater.services.impl;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.cglib.util.StringSwitcher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.wakana.samater.services.JWTService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTServiceImpl  implements JWTService{
    
    
    public String generateToken(UserDetails userDetails){
    
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 365)) // 1 an
                .signWith(getSiginKey(), SignatureAlgorithm.HS256)
                .compact()
                
                ;
                
    }
     public String generateRefreshToken(Map<String, Object> extraClaims,UserDetails userDetails){
    
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 604080000))
                .signWith(getSiginKey(), SignatureAlgorithm.HS256)
                .compact()
                
                ;
                
    }
    
    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }
    private <T> T extractClaim(String token , Function<Claims,T> claimsRosolvers){
        final Claims claims = extractAllClaims(token);
        return claimsRosolvers.apply(claims );
        
    }
    
    
    private Key getSiginKey(){
        byte[] key = Decoders.BASE64.decode("4f1feeca525de4cdb064656007da3edac7895a87ff0ea865693300fb8b6e8f9c");
        return  Keys.hmacShaKeyFor(key);
        
    }
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSiginKey()).build().parseClaimsJws(token).getBody();
        
    }
    
    public boolean isTokenValid(String token , UserDetails userDetails){
        final String username =  extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) );
    }
    private boolean isTokenExpired(String token){
        
        return extractClaim(token,Claims::getExpiration).before(new Date());
    }
}
