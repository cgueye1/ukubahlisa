package com.wakana.realestateworks.dto.v2;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeaveReviewDto {

    @NotNull(message = "La décision est obligatoire (APPROVED ou REJECTED)")
    private String decision; // "APPROVED" or "REJECTED"

    /** Required when decision = REJECTED */
    private String managerComment;
}