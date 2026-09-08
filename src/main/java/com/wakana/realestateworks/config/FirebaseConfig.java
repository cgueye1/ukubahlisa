package com.wakana.realestateworks.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true")
public class FirebaseConfig {
    @Value("${firebase.credentials.path}")
    private String credentialsPath;

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        Resource credentials = new FileSystemResource(credentialsPath);
        if (!credentials.exists()) {
            throw new FileNotFoundException(
                    "Firebase credentials file not found: " + credentialsPath);
        }

        try (InputStream credentialsStream = credentials.getInputStream()) {
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(credentialsStream);
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();
            FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "ter");
            return FirebaseMessaging.getInstance(app);
        }
    }
}