package com.wakana.realestateworks.dto;

import com.wakana.realestateworks.enums.MovementType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MaterialMovementRequest {

    @Schema(description = "ID du matériau concerné")
    private Long materialId;

    @Schema(description = "Quantité à ajouter ou retirer")
    private Double quantity;

    @Schema(description = "Type de mouvement", example = "ENTRY")
    private MovementType type;

    @Schema(description = "Commentaire optionnel sur le mouvement")
    private String comment;
}
