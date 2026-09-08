package com.wakana.realestateworks.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LotDto {
    @Schema(hidden = true)
    private Long id;
    private String name;
    private String description;

    private String startDate; // "dd-MM-yyyy"
    private String endDate; // "dd-MM-yyyy"
    @Schema(hidden = true)
    private String status; // "PENDING", etc.
    private Long realEstatePropertyId;
    private Long subcontractorId;
    @Schema(hidden = true)
    private int progressPercentage = 0;
}
