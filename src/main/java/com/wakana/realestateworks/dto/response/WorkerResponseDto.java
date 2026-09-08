package com.wakana.realestateworks.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkerResponseDto {
    private Long id;
    private String prenom;
    private String nom;
    private String telephone;
    private boolean present; // true si présent aujourd'hui, false sinon
}
