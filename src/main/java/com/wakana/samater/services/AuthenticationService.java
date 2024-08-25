package com.wakana.samater.services;

import com.wakana.samater.dto.ChangePasswordRequest;
import com.wakana.samater.dto.JwtAuthenticationResponse;
import com.wakana.samater.dto.RefreshTokenRequest;
import com.wakana.samater.dto.SignUpRequest;
import com.wakana.samater.dto.SigninRequest;
import com.wakana.samater.model.User;



public interface AuthenticationService {
    User signUp(SignUpRequest signUpRequest);
    JwtAuthenticationResponse signin(SigninRequest signinRequest);
    
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    
   User changePassword(ChangePasswordRequest   changePassword);
   User resetPassword(ChangePasswordRequest   changePassword);
}
