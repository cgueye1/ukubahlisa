package com.wakana.realestateworks.dto;

import lombok.Data;

@Data
public class OTPRequest {
    private String phoneNumber;
    private String otp;
}
