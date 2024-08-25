package com.wakana.samater.services;

import org.springframework.http.ResponseEntity;

public interface WhatsAppImageSenderService {
    public ResponseEntity<String> sendAudio(String recipientPhoneNumber, String medialink);
    
}
