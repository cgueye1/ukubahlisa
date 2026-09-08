package com.wakana.realestateworks.dto;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DocumentRequest {

    @Schema(description = "Titre du document", example = "Plan de masse")
    private String title;

    @Schema(description = "Fichier du document (PDF, image, etc.)")
    private MultipartFile file;

    @Schema(description = "Description du document", example = "Plan de masse du lot B14 en 3D")
    private String description;

    @Schema(description = "ID de la propriété à laquelle le document est rattaché", example = "13")
    private Long realEstatePropertyId;

    @Schema(description = "ID du type de document (UnitParameter)", example = "1")
    private Long typeId;

    @Schema(
        description = "Date de début (format attendu : dd-MM-yyyy)",
        example = "01-07-2025"
    )
    private String startDate;

    @Schema(
        description = "Date de fin (format attendu : dd-MM-yyyy)",
        example = "31-12-2025"
    )
    private String endDate;
}
