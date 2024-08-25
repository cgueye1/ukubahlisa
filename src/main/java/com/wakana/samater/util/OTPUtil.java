package com.wakana.samater.util;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import okhttp3.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class OTPUtil {
    private OkHttpClient client = new OkHttpClient().newBuilder().build();
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String ACCEPT_HEADER = "Accept";
    // Map pour stocker les OTP et leurs durées de validité
    private Map<String, OtpInfo> otpStorage = new HashMap<>();

    // Générer un OTP de 4 chiffres
    public String generateOTP() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }

   // Envoyer l'OTP par SMS
   public void sendOTPBySMS(String phoneNumber, String otp) throws IOException {
  /*  MediaType mediaType = MediaType.parse("application/json");
   String requestBodyString = "{"
   + "\"from\":\"447491163443\","
   + "\"to\":\"" + phoneNumber + "\","
   + "\"messageId\":\"a28dd97c-1ffb-4fcf-99f1-0b557ed381da\","
   + "\"content\":{"
       + "\"text\":\"Votre code OTP est " + otp + "\""
   + "}"
+ "}";
        //RequestBody body = RequestBody.create(mediaType, requestBodyString.getBytes(StandardCharsets.UTF_8));
        RequestBody body = RequestBody.create(requestBodyString,mediaType);


   // RequestBody body = RequestBody.create(mediaType, requestBodyString);
    Request request = new Request.Builder()
            .url("https://vv1zpr.api.infobip.com/whatsapp/1/message/text")
            .post(body)
            .addHeader(AUTHORIZATION_HEADER, "App 03773ac788633b66fa689adbfed09d2e-edb699c8-9cc2-485f-ae24-3ddcea4bd85a")
            .addHeader(CONTENT_TYPE_HEADER, "application/json")
            .addHeader(ACCEPT_HEADER, "application/json")
            .build();

    Response response = client.newCall(request).execute();
    System.out.println("Response: " + response.body().string());
    */

    // Votre logique de gestion de la réponse
    
    OkHttpClient client = new OkHttpClient().newBuilder()
    .build();
MediaType mediaType = MediaType.parse("application/json");
//RequestBody body = RequestBody.create("{\"messages\":[{\"destinations\":[{\"to\":\"" + phoneNumber + "\"}],\"from\":\"Wave\",\"text\":\"Vous avez reçu un dépôt de 2.500.000FCFA de 774262278  Ref:CI240320.0839.A82838. Merci\"}]}", mediaType);
RequestBody body = RequestBody.create("{\"messages\":[{\"destinations\":[{\"to\":\"" + phoneNumber + "\"}],\"from\":\"SamaTer\",\"text\":\"Bonjour voici votre code de validation : " + otp + "\"}]}", mediaType);
Request request = new Request.Builder()
    .url("https://vv1zpr.api.infobip.com/sms/2/text/advanced")
    .method("POST", body)
    .addHeader("Authorization", "App 03773ac788633b66fa689adbfed09d2e-edb699c8-9cc2-485f-ae24-3ddcea4bd85a")
    .addHeader("Content-Type", "application/json")
    .addHeader("Accept", "application/json")
    .build();
Response response = client.newCall(request).execute();
System.out.println("Response: " + response.body().string());
    
}

    // Enregistrer l'OTP avec le numéro de téléphone et sa durée de validité (5 minutes)
    public void storeOTP(String phoneNumber, String otp) {
        otpStorage.put(phoneNumber, new OtpInfo(otp, System.currentTimeMillis() + 5 * 60 * 1000));
    }

    // Valider l'OTP
    public boolean validateOTP(String phoneNumber, String otp) {
        OtpInfo otpInfo = otpStorage.get(phoneNumber);
        return otpInfo != null && otpInfo.getOtp().equals(otp) && otpInfo.getExpiryTime() >= System.currentTimeMillis();
    }

    // Classe pour stocker l'OTP et sa durée de validité
    private static class OtpInfo {
        private String otp;
        private long expiryTime;

        public OtpInfo(String otp, long expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() {
            return otp;
        }

        public long getExpiryTime() {
            return expiryTime;
        }
    }
}
