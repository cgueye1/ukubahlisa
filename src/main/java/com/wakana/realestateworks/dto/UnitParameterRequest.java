package com.wakana.realestateworks.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UnitParameterRequest {

    @Schema(description = "Libellé du paramètre")
    private String label;

    @Schema(description = "Code unique du paramètre")
    private String code;

    @Schema(description = "Indique si une date de début est requise")
    private boolean hasStartDate;

    @Schema(description = "Indique si une date de fin est requise")
    private boolean hasEndDate;

    @Schema(description = "Type de paramètre. Valeurs possibles : DOCUMENT, UNIT, MATERIAL_CATEGORY", example = "DOCUMENT")
    private String type;
}
