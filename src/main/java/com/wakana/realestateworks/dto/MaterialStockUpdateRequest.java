package com.wakana.realestateworks.dto;

import lombok.Data;

@Data
public class MaterialStockUpdateRequest {
    private Long materialId;
    private double quantityToSubtract;
}
