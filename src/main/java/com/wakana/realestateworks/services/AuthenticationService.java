package com.wakana.realestateworks.services;

import com.wakana.realestateworks.dto.ChangePasswordRequest;
import com.wakana.realestateworks.dto.JwtAuthenticationResponse;
import com.wakana.realestateworks.dto.RefreshTokenRequest;
import com.wakana.realestateworks.dto.ResetPasswordRequest;
import com.wakana.realestateworks.dto.SignUpRequest;
import com.wakana.realestateworks.dto.SigninRequest;
import com.wakana.realestateworks.model.User;

public interface AuthenticationService {
    User signUp(SignUpRequest signUpRequest);
    User   saveReservataire(SignUpRequest signUpRequest,Long id);

    User create(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signin(SigninRequest signinRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    User changePassword(ChangePasswordRequest changePassword);
    User changePassword(ChangePasswordRequest changePassword,long id);
    User resetPassword(ResetPasswordRequest  changePassword);
}
