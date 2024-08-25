package com.wakana.samater.dto;


import lombok.Data;

@Data
public class SignUpRequest {
    private String nom;
    private String prenom;
    private String password;
    private boolean activated;
    private String telephone;
    private String role;
    private String date;
    private String lieunaissance;
    private String adress;
    private boolean notifiable;



    
}
