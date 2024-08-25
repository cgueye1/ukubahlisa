package com.wakana.samater.dto;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class VoiceAnnonceRequest {
    private MultipartFile file;
    private String date;
    private String title;
    private String audio;
    private Long timestamp;
    private String descr;   
    private boolean alert;

}
