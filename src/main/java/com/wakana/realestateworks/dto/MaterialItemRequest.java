package com.wakana.realestateworks.dto;

import lombok.Data;

@Data
public class MaterialItemRequest {
    private Long materialId;

    private int quantity;
}