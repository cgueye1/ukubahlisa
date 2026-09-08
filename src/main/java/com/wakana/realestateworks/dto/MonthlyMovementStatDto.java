package com.wakana.realestateworks.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyMovementStatDto {
    private String date; 
    private Double totalEntries;
    private Double totalExits;
}