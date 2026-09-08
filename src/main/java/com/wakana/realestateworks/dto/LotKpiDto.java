package com.wakana.realestateworks.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LotKpiDto {
    private Long pending;
    private Long inProgress;
    private Long completed;
    private Long canceled;
    private Long total;
}