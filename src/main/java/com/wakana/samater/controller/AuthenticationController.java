
package com.wakana.samater.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wakana.samater.dto.ChangePasswordRequest;
import com.wakana.samater.dto.JwtAuthenticationResponse;
import com.wakana.samater.dto.RefreshTokenRequest;
import com.wakana.samater.dto.SignUpRequest;
import com.wakana.samater.dto.SigninRequest;
import com.wakana.samater.model.User;
import com.wakana.samater.repository.UserRepository;
import com.wakana.samater.services.AuthenticationService;

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
    
    
    
   /* @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authenticationService.signUp(signUpRequest));
        
    }*/
    
    @PostMapping("/signup")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest) {
      
        // Vérifiez si le numéro de téléphone existe
        if (userRepository.existsByTelephone(signUpRequest.getTelephone())) {
            String errorMessage = "Le numéro de téléphone existe déjà. Veuillez utiliser un autre numéro de téléphone.";
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(Collections.singletonMap("error", errorMessage));
        }
    
        // Si l'email et le numéro de téléphone sont uniques, appelez le service d'authentification
        return ResponseEntity.ok(authenticationService.signUp(signUpRequest));
    }
    

    //JwtAuthenticationResponse
     @PostMapping("/signin")
     @CrossOrigin(origins = "*")
    public ResponseEntity<?> signin(@RequestBody SigninRequest signInRequest) {
      //  String errorMessage = "Le numero de telephone est incorrecte";

       /*  User user = userRepository.findByTelephone(signInRequest.getEmail());
        if(null == user){
           return ResponseEntity
                .status(HttpStatus.OK)
                .body(Collections.singletonMap("error", errorMessage));  
        }*/
        
       // signInRequest.setEmail(user.getEmail());
        
        return ResponseEntity.ok(authenticationService.signin(signInRequest));
        
    }
    
   
      @PostMapping("/password/reset")
      @CrossOrigin(origins = "*")
    public ResponseEntity<?> resetPassword(@RequestBody ChangePasswordRequest  changePasswordRequest) {
        String errorMessage = "Le numero de telephone est incorrecte";

        User user = userRepository.findByTelephone(changePasswordRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if(null == user){
           return ResponseEntity
                .status(HttpStatus.OK)
                .body(Collections.singletonMap("error", errorMessage));  
        }
        
    
        
        return ResponseEntity.ok(authenticationService.resetPassword(changePasswordRequest));
        
    }
       @PostMapping("/refresh")
       @CrossOrigin(origins = "*")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
        
    }
    
   
    
}
