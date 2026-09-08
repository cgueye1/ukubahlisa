package com.wakana.realestateworks.dto;

import lombok.Data;

@Data
public class ReservationRequest {
    private long userId;  
    private long propertyId;  
}
