package com.wakana.samater.dto;
import lombok.Data;

@Data
public class ExpressionRequest {
    private String libelle;
    private String code;
    private String voicename;
    private Long categorieId;
    private Long voicelangueId;
    
}
