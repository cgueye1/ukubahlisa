package com.wakana.realestateworks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstructionStatusKpiDto {
    private long total;
    private long inProgress;
    private long delayed;
    private long pending;
    private long completed;

/* *   private double inProgressPercentage;
    private double delayedPercentage;
    private double pendingPercentage;
    private double completedPercentage;*/
}
