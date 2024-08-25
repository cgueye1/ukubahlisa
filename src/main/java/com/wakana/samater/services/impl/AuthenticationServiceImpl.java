package com.wakana.samater.services.impl;
import java.util.HashMap;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wakana.samater.dto.ChangePasswordRequest;
import com.wakana.samater.dto.JwtAuthenticationResponse;
import com.wakana.samater.dto.RefreshTokenRequest;
import com.wakana.samater.dto.SignUpRequest;
import com.wakana.samater.dto.SigninRequest;
import com.wakana.samater.model.Carte;
import com.wakana.samater.model.Role;
import com.wakana.samater.model.TypeAbonnement;
import com.wakana.samater.model.User;
import com.wakana.samater.repository.CarteRepository;
import com.wakana.samater.repository.UserRepository;
import com.wakana.samater.services.ApiKeyService;
import com.wakana.samater.services.AuthenticationService;
import com.wakana.samater.services.JWTService;
import com.wakana.samater.util.QRCodeUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
    
 private final UserRepository userRepository;
 private final PasswordEncoder passwordEncoder;
 private final AuthenticationManager authenticationManager;
 private final JWTService jwtService;
  private final ApiKeyService  apiKeyService;
    private final CarteRepository carteRepository;
    
    
    public static String generateUniqueCardNumber(long userId) {
      // Ajoutez un décalage à l'ID de l'utilisateur pour garantir un nombre à 9 chiffres
      long shiftedId = userId + 100000000;

      // Convertir l'ID de l'utilisateur en une chaîne de caractères
      String userIdString = Long.toString(shiftedId);

      // Prenez les 9 derniers chiffres de la chaîne pour obtenir un nombre de 9 chiffres
      String uniqueCardNumber = userIdString.substring(userIdString.length() - 9);

      return uniqueCardNumber;
  }
 
 public User signUp(SignUpRequest signUpRequest){

      User user =  User.fromSignUpRequest(signUpRequest);
      user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
      User savedUser = userRepository.save(user);
      
      
      Carte carte = new Carte();
      carte.setUserId(savedUser.getId());
      carte.setNombre_voyage(0);
      carte.setNumero(generateUniqueCardNumber(savedUser.getId()));
      carte.setType_abonnement(TypeAbonnement.FORFAIT);
      carte.setQrcode(QRCodeUtil.generateQrCode(String.format("%04d",savedUser.getId())));
      carteRepository.save(carte);
      
  
     
      return savedUser;
     
     
     
 }
 
  public User changePassword(ChangePasswordRequest   changePassword){

        User user = userRepository.findByTelephone(changePassword.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        user.setPassword(passwordEncoder.encode(changePassword.getNewpassword()));
 
        return userRepository.save(user);
     
     
     
 }
    public User resetPassword(ChangePasswordRequest   changePassword){
      User user = userRepository.findByTelephone(changePassword.getEmail()).orElseThrow(() -> new IllegalArgumentException("Téléphone incorrecte"));
      user.setPassword(passwordEncoder.encode(changePassword.getNewpassword()));

      return userRepository.save(user);
     
     
     
 }
 public JwtAuthenticationResponse signin(SigninRequest signinRequest) {
     
       authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));
       var user = isEmail(signinRequest.getEmail()) ? 
       userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password")) : 
       userRepository.findByTelephone(signinRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
       
          var jwt = jwtService.generateToken(user);
       var refreshToken = jwtService.generateRefreshToken(new HashMap<>(),user);
       JwtAuthenticationResponse  jwtAuthenticationResponse = new JwtAuthenticationResponse();
       jwtAuthenticationResponse.setToken(jwt);
       jwtAuthenticationResponse.setRefreshToken(refreshToken );
       return jwtAuthenticationResponse;
       
     
 }
 private boolean isEmail(String input) {
  return input != null && input.contains("@");
}
 public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
     String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
     User user = userRepository.findByTelephone(userEmail).orElseThrow();
     if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
         var jwt = jwtService.generateToken(user);
             JwtAuthenticationResponse  jwtAuthenticationResponse = new JwtAuthenticationResponse();
       jwtAuthenticationResponse.setToken(jwt);
       jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken() );
       return jwtAuthenticationResponse;
         
     }
     return null;
     
 }
    
}
