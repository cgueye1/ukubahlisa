package com.wakana.samater.services;


import org.springframework.stereotype.Service;



@Service
public interface ApiKeyService {

    



    public String generateApiKey();

    public boolean isValidApiKey(String clientId, String apiKey) ;

    public void revokeApiKey(String clientId) ;
}
