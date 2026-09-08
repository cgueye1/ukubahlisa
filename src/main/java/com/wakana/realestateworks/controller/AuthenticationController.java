
package com.wakana.realestateworks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wakana.realestateworks.dto.ChangePasswordRequest;
import com.wakana.realestateworks.dto.JwtAuthenticationResponse;
import com.wakana.realestateworks.dto.RefreshTokenRequest;
import com.wakana.realestateworks.dto.ResetPasswordRequest;
import com.wakana.realestateworks.dto.SignUpRequest;
import com.wakana.realestateworks.dto.SigninRequest;
import com.wakana.realestateworks.model.User;
import com.wakana.realestateworks.repository.UserRepository;
import com.wakana.realestateworks.services.AuthenticationService;

import java.util.Collections;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> signup(@ModelAttribute SignUpRequest signUpRequest) {
        try {
            User savedUser = authenticationService.signUp(signUpRequest);
            return ResponseEntity.ok(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * @PostMapping("/reservataire/{id}")
     * public ResponseEntity<?> saveReservataire(@RequestBody SignUpRequest
     * signUpRequest, @PathVariable Long id) {
     * try {
     * User savedUser = authenticationService.saveReservataire(signUpRequest, id);
     * return ResponseEntity.ok(savedUser);
     * } catch (IllegalArgumentException e) {
     * return ResponseEntity.badRequest().body(e.getMessage());
     * }
     * }
     */

    // JwtAuthenticationResponse
    @PostMapping("/signin")

    public ResponseEntity<?> signin(@RequestBody SigninRequest signInRequest) {

        return ResponseEntity.ok(authenticationService.signin(signInRequest));

    }

    @PutMapping("/cahnge-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            User updatedUser = authenticationService.changePassword(changePasswordRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating password");
        }
    }

    @PostMapping("/password/reset")

    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest changePasswordRequest) {
        String errorMessage = "Le numero de telephone est incorrecte";

        User user = userRepository.findByTelephone(changePasswordRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (null == user) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Collections.singletonMap("error", errorMessage));
        }

        return ResponseEntity.ok(authenticationService.resetPassword(changePasswordRequest));

    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));

    }

}
