package com.wakana.realestateworks.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserRequest {
    private String nom;
    private String prenom;
    private String email;
    @Schema(hidden = true)
    private String password;

    private String telephone;
    private String date;
    private String lieunaissance;
    private String adress;

    @Schema(hidden = true)
    private boolean activated;
    @Schema(hidden = true)
    private String role;

    @Schema(description = "")
    private String profil; // Fixée en interne

    @Schema(hidden = true)
    private boolean notifiable;

    @Schema(hidden = true)
    private String subject;

    @Schema(hidden = true)
    private String html;

    @Schema(hidden = true)
    private String compagnyName;

    @Schema(hidden = true)
    private double funds;

    @Schema(hidden = true)
    private Long propretyId;

    @Schema(hidden = true)
    private Long userId;

    @Schema(hidden = true)
    private double area;

    @Schema(hidden = true)
    private Long jobId;

    
    private MultipartFile photo;
}
