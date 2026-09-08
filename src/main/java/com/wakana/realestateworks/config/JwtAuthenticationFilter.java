package com.wakana.realestateworks.config;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wakana.realestateworks.services.JWTService;
import com.wakana.realestateworks.services.impl.UserServiceImpl;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor

public class JwtAuthenticationFilter  extends OncePerRequestFilter{
    
    
    private final JWTService jwtService;
    private final UserServiceImpl userservice;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                String authHeader = request.getHeader("Authorization");
                final String jwt;
                final String userEmail;
                

        

                if(StringUtils.isEmpty(authHeader )  ||  !org.apache.commons.lang3.StringUtils.startsWith(authHeader , "Bearer ") || authHeader==null   ){
                    
                    filterChain.doFilter(request, response);
                    return ;
                } 
                jwt = authHeader.substring(7);
                userEmail = jwtService.extractUserName(jwt);
                
                
                if(StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails  userdetails = userservice.userDetailsService().loadUserByUsername(userEmail);
                    if(jwtService.isTokenValid(jwt, userdetails)){
                        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                            userdetails, null, userdetails.getAuthorities()
                        );
                        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        securityContext.setAuthentication(token);
                        SecurityContextHolder.setContext(securityContext);
                        
                    }
                }
                
                filterChain.doFilter(request, response);
                
                
    }
    
}
