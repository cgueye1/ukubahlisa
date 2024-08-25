package com.wakana.samater.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class ApiKeyAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public ApiKeyAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
