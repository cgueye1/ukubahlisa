package com.wakana.samater.dto;

import lombok.Data;

@Data
public class NearbyFacilityRequest {
    private String descr;
    private double lat;
    private double lon;
    private String libelle;
    private String nearbyFacilityType;
}
