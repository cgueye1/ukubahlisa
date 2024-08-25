package com.wakana.samater.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wakana.samater.services.WhatsAppImageSenderService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;


@Service
@RequiredArgsConstructor
public class WhatsAppImageSenderServiceImpl implements WhatsAppImageSenderService{
      private static final String API_URL = "https://graph.facebook.com/v19.0/216824018189386/messages";
    private static final String ACCESS_TOKEN = "EAASioxTnWuQBO3ErfHLTDd8GBAIATjMfjMdcJcMwUgydrsMc1AMfdNZAyr7NU0OdhVZBXC2l24EKpBMxrlObmouB51YGEmjv2jtNCxZCNn33hqKobPdGjZB2I3O80nEDZAXX1NZBRfmIQzXomS6FGWQaC50G7Pb8h1yB34t6PE2jOUyc5LDZCxSQZBqYSCJuaZCWAgtAdnHE5nLLw5rm8GQ4ZD";

    public ResponseEntity<String> sendAudio(String recipientPhoneNumber, String medialink) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ACCESS_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\n" +
                "  \"messaging_product\": \"whatsapp\",\n" +
                "  \"recipient_type\": \"individual\",\n" +
                "  \"to\": \"" + recipientPhoneNumber + "\",\n" +
                "  \"type\": \"audio\",\n" +
                "  \"audio\": {\n" +
                "    \"link\": \"" + medialink+ "\"\n" +
                "  }\n" +
                "}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(API_URL, requestEntity, String.class);
        return responseEntity;
    }
    
    
    
}
