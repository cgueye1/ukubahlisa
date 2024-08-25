package com.wakana.samater.dto;


import lombok.Data;

@Data
public class CarteRequest {
    private Long id_zone_inter;
    private Long id_user;
    private String numero;
    private String type_abonnement;
    private int nombre_voyage;
    private int classe;
}
