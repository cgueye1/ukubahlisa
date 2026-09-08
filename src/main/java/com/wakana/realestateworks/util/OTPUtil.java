package com.wakana.realestateworks.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

@Component
public class OTPUtil {
    
    @Value("${app.sms.url}")
    private String url;

    @Value("${app.sms.private.key}")
    private String privateKey;

    @Value("${app.sms.token}")
    private String token;
    
    @Value("${app.sms.signature}")
    private String signature;
    
    @Value("${app.sms.login}")
    private String login;

    
    

    private Map<String, OtpInfo> otpStorage = new HashMap<>();

    public String generateOTP() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }


    public void storeOTP(String phoneNumber, String otp) {
        otpStorage.put(phoneNumber, new OtpInfo(otp, System.currentTimeMillis() + 5 * 60 * 1000));
    }

    public boolean validateOTP(String phoneNumber, String otp) {
        OtpInfo otpInfo = otpStorage.get(phoneNumber);
        return otpInfo != null && otpInfo.getOtp().equals(otp) && otpInfo.getExpiryTime() >= System.currentTimeMillis();
    }

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
    

     
    
    
    public void sendSms( String subject, String recipient, String content) throws Exception {
        String apiUrl = url;
        String password = token;
        String authString = login + ":" + password;
        String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());
        System.out.println(apiUrl);
        System.out.println(password );
        System.out.println(authString);
        System.out.println(authStringEnc);

     content = URLEncoder.encode(content, "UTF-8");
        subject = URLEncoder.encode(subject, "UTF-8");
        signature = URLEncoder.encode(signature, "UTF-8");

        long timestamp = System.currentTimeMillis() / 1000;
        String messageToEncrypt = token + subject + signature + recipient + content + timestamp;

        String key = hmacSha(privateKey, messageToEncrypt);

        String url = apiUrl + "?token=" + token + "&subject=" + subject + "&signature=" + signature +
                     "&recipient=" + recipient + "&content=" + content + "&timestamp=" + timestamp +
                     "&key=" + key;

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestProperty("Authorization", "Basic " + authStringEnc);

        int responseCode = con.getResponseCode();
        if (responseCode == 401) {
            throw new RuntimeException("Login or token incorrect: HTTP error code : " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

      System.out.println(response.toString());
    }

    public static String hmacSha(String secretKey, String value) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(value.getBytes("UTF-8"));
        
        StringBuilder hexResult = new StringBuilder();
        for (byte b : rawHmac) {
            hexResult.append(String.format("%02x", b));
        }
        return hexResult.toString();
    }
    
}
