package com.wakana.realestateworks.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GlobalBudgetKpiDto {
    private double totalPlanned;
    private double totalConsumed;
     private double totalRemaining;
    private double consumedPercentage;
    private double remainingPercentage;
}
