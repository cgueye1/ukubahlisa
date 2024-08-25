package com.wakana.samater.controller;
import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.wakana.samater.dto.OTPRequest;
import com.wakana.samater.util.OTPUtil;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/otp")
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class OtpController {

    @Autowired
    private OTPUtil otpUtil;

    @PostMapping("/generate")
 ResponseEntity<?>  generateAndSendOTP(@RequestBody OTPRequest oTPRequests) {
        String otp = otpUtil.generateOTP();
        try {
            otpUtil.sendOTPBySMS(oTPRequests.getPhoneNumber(), otp);
            otpUtil.storeOTP(oTPRequests.getPhoneNumber(), otp);
   
                        return ResponseEntity.ok(Collections.singletonMap("otp", otp.toString()));

        } catch (IOException e) {
            e.printStackTrace();
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
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
}
