package com.wakana.samater.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.wakana.samater.model.Role;
import com.wakana.samater.services.UserSeervice;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserSeervice userSeervice;
   
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        
        http.csrf(AbstractHttpConfigurer::disable)
                 .authorizeHttpRequests(request -> request.requestMatchers("/api/v1/auth/**")
                                        .permitAll() 
                                        .requestMatchers("/api/files/**").permitAll()
                                        .requestMatchers("/api/actu/**").permitAll()
                                        .requestMatchers("/api/gare/**").permitAll()
                                        .requestMatchers("/api/otp/**").permitAll()
                                        .requestMatchers("/api/v1/vod/**").permitAll()
                                        .requestMatchers("/chat/**").permitAll()
                                        .requestMatchers("/messages/**").permitAll()
                                       
                                        
                                        
                                        
                                        .requestMatchers("/ws/**").permitAll()
                                         .requestMatchers("/swagger-ui/**").permitAll()
                                         .requestMatchers("/swagger-ui/**",
                                         "/swagger-resources/*",
                                         "/v3/api-docs/**")
                                 .permitAll()
                                        .requestMatchers("/api/v1/parameters/**").permitAll()
                                        .requestMatchers("/api/v1/user/**").permitAll()
                                        .anyRequest().authenticated())
                            .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                            .authenticationProvider(authenticationProvider()).addFilterBefore(
                                jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                            );
                            
                            return http.build();
                                        
                                        
                                        
                 
                 
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider .setUserDetailsService(userSeervice.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider ;
    }
     
    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }
    
    
    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }
}
 // .requestMatchers("/api/v1/user/**").hasAnyAuthority(Role.USER.name())