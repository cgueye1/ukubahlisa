package com.wakana.realestateworks.dto.response;


import com.wakana.realestateworks.enums.TaskStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskStatusKpiResponse {
    private TaskStatusEnum status;
    private Double percentage;
}
