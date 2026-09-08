package com.wakana.realestateworks.controller;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wakana.realestateworks.dto.OTPRequest;
import com.wakana.realestateworks.util.OTPUtil;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@Hidden
@RequestMapping("/api/otp")
@RestController
@RequiredArgsConstructor
public class OtpController {

    @Autowired
    private OTPUtil otpUtil;

    @PostMapping("/generate")
    ResponseEntity<?> generateAndSendOTP(@RequestBody OTPRequest oTPRequests) throws Exception {
        String otp = otpUtil.generateOTP();
        try {

            otpUtil.sendSms(
                    "OTP", oTPRequests.getPhoneNumber(),
                    "Votre code secret pour valider votre inscription est : " + otp);
            otpUtil.storeOTP(oTPRequests.getPhoneNumber(), otp);

            return ResponseEntity.ok(Collections.singletonMap("otp", otp.toString()));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Échec d'envoi: " + e.getMessage());
        }

    }

    @PostMapping("/validate")
    public boolean validateOTP(@RequestBody OTPRequest oTPRequests) {
        if (otpUtil.validateOTP(oTPRequests.getPhoneNumber(), oTPRequests.getOtp())) {
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/send")
    public ResponseEntity<?> getAll() throws Exception {
        otpUtil.sendSms(
                "OTP", "2217785386140", "Salam RAS ");
        return ResponseEntity.ok("send");

    }

}
