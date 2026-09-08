package com.wakana.realestateworks.dto.response;

import com.wakana.realestateworks.enums.TaskPriorityEnum;
import com.wakana.realestateworks.enums.TaskStatusEnum;
import com.wakana.realestateworks.model.TaskDocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private TaskPriorityEnum priority;
    private TaskStatusEnum status;
    private RealEstateResponseDto realEstateProperty;
    private List<UserResponseDto> executors;
    private List<String> pictures;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private  List<TaskDocument> documents;
}
