package com.wakana.realestateworks.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CriticalTaskDto {
    private Long id;
    private LocalDate endDate;
    private String title;
    private String status;
    private String priority;
    private String color;
    private String statusLabel; // "En retard", "Urgent", "À jour"
}
