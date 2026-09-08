package com.wakana.realestateworks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CriticalMaterialStockDto {
    private Long id;
    private String label;
    private double quantity;
    private double criticalThreshold;
    private String unitName;
    private String propertyName;
    private String statusLabel;
    private String color;
}
