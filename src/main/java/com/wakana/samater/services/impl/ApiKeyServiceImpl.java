package com.wakana.samater.services.impl;

import org.springframework.stereotype.Service;

import com.wakana.samater.services.ApiKeyService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ApiKeyServiceImpl implements  ApiKeyService {

    private final Map<String, String> apiKeys = new HashMap<>();

    public String generateApiKey() {
        String apiKey = UUID.randomUUID().toString();
       // apiKeys.put(clientId, apiKey);
        return apiKey;
    }

    public boolean isValidApiKey(String clientId, String apiKey) {
        return apiKey.equals(apiKeys.get(clientId));
    }

    public void revokeApiKey(String clientId) {
        apiKeys.remove(clientId);
    }
}
