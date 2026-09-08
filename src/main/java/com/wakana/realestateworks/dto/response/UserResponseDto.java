package com.wakana.realestateworks.dto.response;

import com.wakana.realestateworks.enums.ProfilEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String prenom;
    private String nom;
    private String telephone;
    private String photo;
    private ProfilEnum profil;
}
