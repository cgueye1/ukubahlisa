package com.wakana.realestateworks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExecutorDashboardResponse {
    private int totalTasks;
    private int completedTasks;
    private double performancePercentage;
}
