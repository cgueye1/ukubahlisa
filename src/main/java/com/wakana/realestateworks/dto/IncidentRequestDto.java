package com.wakana.realestateworks.dto;
import lombok.Data;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class IncidentRequestDto {
    private String title;
    private String description;
    private Long propertyId;
    private List<MultipartFile> pictures;
}
