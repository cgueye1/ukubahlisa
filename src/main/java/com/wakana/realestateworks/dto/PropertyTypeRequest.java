package com.wakana.realestateworks.dto;

import lombok.Data;

@Data
public class PropertyTypeRequest {

    private String typeName; 

    private boolean isParent;
}
