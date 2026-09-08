package com.wakana.realestateworks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor   // 👈 Ajoute ça

@AllArgsConstructor
public class PointingAddressRequest {
    private double latitude;
    private double longitude;
    private String name;
    private String qrcode;

}
