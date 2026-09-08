package com.wakana.realestateworks.dto;

import lombok.Data;

@Data
public class MaterialStockRequest {
    private String label;
    private double quantity;
    private double criticalThreshold;
    private Long unitId;
    private Long propertyId;
}
