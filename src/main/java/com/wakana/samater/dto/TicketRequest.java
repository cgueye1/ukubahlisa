package com.wakana.samater.dto;

import lombok.Data;

@Data
public class TicketRequest {
    private Long idUser;
    private String produit;
    private String mode_paiement;
    private String reference;
    private boolean allersimple;
    private Double duree;
    private int classe;
    private Long idZoneInter;
}
