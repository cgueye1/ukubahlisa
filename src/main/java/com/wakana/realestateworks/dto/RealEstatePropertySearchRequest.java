package com.wakana.realestateworks.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RealEstatePropertySearchRequest {
    private Long promoterId; // ID du promoteur
    private String name;

    @Schema(hidden = true)
    private Long parentPropertyId; // ID du parent property
    @Schema(hidden = true)
    private Long propertyTypeId;
    @Schema(hidden = true)
    private Double minPrice;
    @Schema(hidden = true)
    private Double maxPrice;
    @Schema(hidden = true)
    private String address;
    @Schema(hidden = true)
    private String latitude; // Latitude for location-based search
    @Schema(hidden = true)
    private String longitude; // Longitude for location-based search

}
