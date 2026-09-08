package com.wakana.realestateworks.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.wakana.realestateworks.enums.ConstructionStatusEnum;
import com.wakana.realestateworks.enums.RealEstatePropertyStatusEnum;
import com.wakana.realestateworks.model.PropertyType;
import com.wakana.realestateworks.model.RealEstateProperty;
import com.wakana.realestateworks.model.User;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Data
public class RealEstatePropertyResponse {
    private Long id;
    private String name;
    private String number;
    private String address;
    private Double area;
    private String latitude;
    private String longitude;
    private boolean isAvailable;
    private Double reservationFee;
    private String description;
    private Integer numberOfLots;
    private double discount;
    private double budget;
    private int level; // The level (floor) of the apartment
    // Équipements communs
    private PropertyType propertyType;
    private User promoter;
    private User recipient;
    private List<String> pictures;
    private String plan;
    private String legalStatus;
    private RealEstateProperty parentProperty;
    private RealEstatePropertyStatusEnum status;
    private String qrcode;


}
