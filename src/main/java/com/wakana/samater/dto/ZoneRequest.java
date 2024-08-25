package com.wakana.samater.dto;

import lombok.Data;

@Data
public class ZoneRequest {
    private Long gare_depart_id;
    private Long gare_arrivee_id;
    private String libelle;
    private Double prix_ticket_first;
    private Double prix_ticket_finzone;
    private Double prix_ticket_second;
    //prix abonnement
    private Double prix_abo_hebdo_first;
    private Double prix_abo_hebdo_second;
    private Double prix_abo_mensuel_first;
    private Double prix_abo_mensuel_second;
    //
    private Double prix_abo_hebdo_10_22;
    private Double prix_abo_mensuel_10_22;
    private Double prix_abo_mensuel_moins_10;
    private Double prix_abo_hebdo_moins_10;
    //
    private int voie;
    private Double duree;
    
}

