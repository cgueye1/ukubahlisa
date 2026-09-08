package com.wakana.realestateworks.dto;

import lombok.Data;
import java.util.List;

@Data
public class MaterialOrderRequest {
    private Long supplierId;
    private List<MaterialItemRequest> materials;
}