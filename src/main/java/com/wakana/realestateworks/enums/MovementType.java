package com.wakana.realestateworks.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true, description = "Type de mouvement : entrée ou sortie de stock")
public enum MovementType {
    @Schema(description = "Entrée de stock")
    ENTRY,

    @Schema(description = "Sortie de stock")
    EXIT
}
