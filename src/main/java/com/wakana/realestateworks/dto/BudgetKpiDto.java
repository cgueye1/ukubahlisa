package com.wakana.realestateworks.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BudgetKpiDto {
    private double consumedPercentage;
    private double remainingPercentage;
}
