package com.wakana.samater.dto;
import lombok.Data;

@Data
public class FeedBackRequest {
    private Long   idAuteur;
    private String titre;
    private String message;  
    private double note;
    private String date;
}
