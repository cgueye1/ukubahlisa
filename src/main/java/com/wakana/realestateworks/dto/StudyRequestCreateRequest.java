package com.wakana.realestateworks.dto;

import lombok.Data;

@Data
public class StudyRequestCreateRequest {
    private String title;
    private String description;
    private Long propertyId; // RealEstateProperty id
    private Long clientId;   // Maitre d’ouvrage
    private Long betId;      // BET
}