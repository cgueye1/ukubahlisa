package com.wakana.realestateworks.services.impl;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wakana.realestateworks.dto.ChangePasswordRequest;
import com.wakana.realestateworks.dto.JwtAuthenticationResponse;
import com.wakana.realestateworks.dto.RefreshTokenRequest;
import com.wakana.realestateworks.dto.ReservationRequest;
import com.wakana.realestateworks.dto.ResetPasswordRequest;
import com.wakana.realestateworks.dto.SignUpRequest;
import com.wakana.realestateworks.dto.SigninRequest;
import com.wakana.realestateworks.enums.ProfilEnum;
import com.wakana.realestateworks.enums.SubscriptionPlanEnum;

import com.wakana.realestateworks.model.RealEstateProperty;

import com.wakana.realestateworks.model.SubscriptionPlan;
import com.wakana.realestateworks.model.User;
import com.wakana.realestateworks.repository.RealEstatePropertyRepository;
import com.wakana.realestateworks.repository.SubscriptionPlanRepository;
import com.wakana.realestateworks.repository.UserRepository;
import com.wakana.realestateworks.services.AuthenticationService;

import com.wakana.realestateworks.services.EmailService;
import com.wakana.realestateworks.services.JWTService;
import com.wakana.realestateworks.services.RealEstatePropertyService;

import com.wakana.realestateworks.services.SubscriptionService;
import com.wakana.realestateworks.util.PasswordUtil;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final SubscriptionService subscriptionService;
    private final RealEstatePropertyRepository realEstatePropertyRepository;

    private final RealEstatePropertyService realEstatePropertyService;

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private EmailService emailService;

    public static String generateUniqueCardNumber(long userId) {
        long shiftedId = userId + 100000000;
        String userIdString = Long.toString(shiftedId);
        String uniqueCardNumber = userIdString.substring(userIdString.length() - 9);
        return uniqueCardNumber;
    }

    @Transactional
    public User signUp(SignUpRequest signUpRequest) {
        if (signUpRequest.getEmail() != null && userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {

            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà.");
        }
        if (userRepository.findByTelephone(signUpRequest.getTelephone()).isPresent()) {
            throw new IllegalArgumentException("Un utilisateur avec ce numéro de téléphone existe déjà.");
        }
        if (signUpRequest.getEmail() != null && !signUpRequest.getEmail().isBlank()) {
            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                throw new RuntimeException("Un utilisateur avec cet e-mail existe déjà.");
            }
        }
        // Création de l'utilisateur
        User user = User.fromSignUpRequest(signUpRequest);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        // Sauvegarder l'utilisateur
        User savedUser = userRepository.save(user);
        userRepository.flush();

        return savedUser;
    }

    public User changePassword(ChangePasswordRequest changePassword) {

        User user = userRepository.findByTelephone(changePassword.getEmail())
                .orElseGet(() -> userRepository.findByEmail(changePassword.getEmail())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid email or phone")));

        // ✅ Vérification de l'ancien mot de passe
        if (!passwordEncoder.matches(
                changePassword.getPassword(),
                user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // ✅ Encodage du nouveau mot de passe
        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));

        return userRepository.save(user);
    }

    public User resetPassword(ResetPasswordRequest changePassword) {
        User user = userRepository.findByTelephone(changePassword.getEmail())
                .orElseGet(() -> userRepository.findByEmail(changePassword.getEmail())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid email or call")));
        String newPassword = PasswordUtil.generateRandomPassword();

        user.setPassword(passwordEncoder.encode(newPassword));
        String html = String.format(
                "<p>Bonjour %s %s</p>" +
                        "<p>Votre mot de passe temporaire est : <strong>%s</strong></p>" +
                        "<p>Cordialement,<br>L'équipe de BTP 360</p>",
                user.getPrenom(), user.getNom(), newPassword);
        emailService.sendHtmlMessage(changePassword.getEmail(), "Mot de passe ", html,
                null);
        return userRepository.save(user);

    }

    public JwtAuthenticationResponse signin(SigninRequest signinRequest) {
        String loginInput = signinRequest.getEmail();
        String rawPassword = signinRequest.getPassword();

        User user;
        if (isEmail(loginInput)) {
            user = userRepository.findByEmail(loginInput)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        } else {
            user = userRepository.findByTelephone(loginInput)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid telephone number or password"));
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email/telephone or password");
        }

        String jwt = jwtService.generateToken(user, user.getId(), user.getProfil());
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user, user.getId(), user.getProfil());
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        return jwtAuthenticationResponse;
    }

    private boolean isEmail(String input) {
        return input != null && input.contains("@");
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByTelephone(userEmail).orElseThrow();
        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtService.generateToken(user, user.getId(), user.getProfil());
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;

        }
        return null;

    }

    public User changePassword(ChangePasswordRequest request, long userId) {

        // 1. Récupération de l'utilisateur
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // 2. Vérification de l'ancien mot de passe
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe actuel incorrect");
        }

        // 3. Vérifier que le nouveau mot de passe est différent de l'ancien
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("Le nouveau mot de passe doit être différent de l'ancien");
        }

        // 4. Encodage et mise à jour du mot de passe
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // 5. Sauvegarde
        return userRepository.save(user);
    }

    @Override
    public User saveReservataire(SignUpRequest signUpRequest, Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveReservataire'");
    }

    @Override
    public User create(SignUpRequest signUpRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

}
