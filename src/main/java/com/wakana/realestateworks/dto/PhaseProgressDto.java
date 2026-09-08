package com.wakana.realestateworks.dto;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class PhaseProgressDto {
    private String phaseName;
    private int averageProgressPercentage;

}

