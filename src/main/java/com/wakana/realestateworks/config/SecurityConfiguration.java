package com.wakana.realestateworks.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.wakana.realestateworks.services.UserSeervice;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserSeervice userService;
    private final SubscriptionFilter subscriptionFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/api/files/**",
                                "/api/job-requests/**",
                                "/api/notifications/**",
                                "/api/rental-invoices/**",
                                "/api/unit-parameters/**",
                                "/api/jobs/**",
                                "/api/ownerpropertyare/**",
                                "/api/call-for-charges/**",
                                "/api/actu/**",
                                "/api/gare/**",
                                "/api/otp/**",
                                "/api/meets/**",
                                "/api/peytech/**",
                                "/ipn/**",
                                "/api/v1/vod/**",
                                "/api/partners/**",

                                "/chat/**",
                                "/api/messages/**",
                                "/api/subscription-plans/**",
                                "/api/subscriptions/**",
                                "/api/companies/**",
                                "/api/indicators/**",
                                "/api/information-sheets/**",
                                "/api/progress-album/**",
                                "/api/realestate/**",
                                "/api/reservations/**",
                                "/api/property-types/**",
                                "/api/v1/notifications/**",
                                "/api/notifications/**",
                                "/api/zone/**",
                                "/api/fare/**",
                                "/chat-websocket/**",

                                "/api/v1/parameters/**",
                                "/api/rapports/**",
                                "/api/tasks/**",
                                "/api/study-requests/**",

                                "/api/materials/**",
                                "/api/budgets/**",

                                "/api/expenses/**",
                                "/api/rapports/**",

                                "/api/orders/**",

                                "/api/incidents/**",
                                "/api/qrcode/*",

                                "/api/workers/**",
                                "/api/lots/**",
                                "/api/documents/**",
                                "/api/pointing-addresses/**",
                                "/api/v1/attendance/**",
                                "/api/v1/real-estates/{realEstateId}/leaves/**",

                                "/api/v1/user/**",

                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger-resources",
                                "/swagger-resources/**"

                        )
                        .permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                // .addFilterBefore(subscriptionFilter,
                // UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
