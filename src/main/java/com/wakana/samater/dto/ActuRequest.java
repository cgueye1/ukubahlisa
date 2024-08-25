package com.wakana.samater.dto;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ActuRequest {
    private MultipartFile file;
    private String date;
    private String title;
    private String img;
    private String link;
    private String description;  
}
