package com.wakana.realestateworks.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskRequest {

    @Schema(description = "Titre de la tâche")
    private String title;

    @Schema(description = "Description de la tâche")
    private String description;

    @Schema(description = "Priorité de la tâche", example = "HIGH", allowableValues = { "HIGH", "MEDIUM", "LOW" })
    private String priority;

    @Schema(description = "Statut de la tâche (Exemple: PENDING, IN_PROGRESS, COMPLETED)", hidden = true)
    private String status = "TODO";

    @Schema(description = "MM-DD-YYYY", required = true)
    private String startDate;

    @Schema(description = "MM-DD-YYYY", required = true)
    private String endDate;

    @Schema(description = "ID du bien immobilier associé")
    private Long realEstatePropertyId;

    @Schema(description = "Liste des ID des exécuteurs")
    private List<Long> executorIds;

    @Schema(description = "Liste des fichiers images à attacher à la tâche")
    private List<MultipartFile> pictures;
}
