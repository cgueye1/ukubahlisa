package com.wakana.realestateworks.dto;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data

public class ExpenseRequest {

    private String description;
    @Schema(description = "MM-DD-YYYY", required = true)
    private String date;

    private Double amount;

    private Long budgetId;

    private MultipartFile evidence;
}
