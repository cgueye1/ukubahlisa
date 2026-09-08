package com.wakana.realestateworks.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class IncidentResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private String propertyName;
    private List<String> pictures;
}
