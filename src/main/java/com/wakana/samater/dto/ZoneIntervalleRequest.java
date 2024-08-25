package com.wakana.samater.dto;

import lombok.Data;

@Data
public class ZoneIntervalleRequest {
    private Long gare_depart_id;
    private Long gare_arrivee_id;
    private Long zone_id;
    private boolean m_fin_de_zone;
    private int voie;
    private Double duree;
    
}

