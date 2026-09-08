package com.wakana.realestateworks.dto.v2.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceSummaryDto {

    private Long workerId;
    private String workerFirstName;
    private String workerLastName;

    private double totalAccruedDays;
    private double usedDays;
    private double pendingDays;
    private double remainingDays;

    private List<LeaveRequestResponseDto> history;
}