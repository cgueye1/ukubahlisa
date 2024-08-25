package com.wakana.samater.dto;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String email;
    private String password;
    private String newpassword;
    
}
