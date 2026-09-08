package com.wakana.realestateworks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopMaterialUsageDto {
    private String materialLabel;
    private Double totalUsedQuantity;
}
