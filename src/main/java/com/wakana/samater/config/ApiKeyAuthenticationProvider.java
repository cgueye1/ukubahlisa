package com.wakana.samater.config;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.wakana.samater.services.ApiKeyService;


public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    private final ApiKeyService apiKeyService;

    public ApiKeyAuthenticationProvider(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String clientId = authentication.getName();
        String apiKey = authentication.getCredentials().toString();

        if (apiKeyService.isValidApiKey(clientId, apiKey)) {
            return new ApiKeyAuthenticationToken(clientId, apiKey);
        } else {
            throw new ApiKeyAuthenticationException("Invalid API key");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
