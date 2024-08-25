package com.wakana.samater.dto;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class LostRequest {
    private MultipartFile file;
    private String firstname;
    private String lastname;
    private String img;
    private String call;       
    private String date;   
    private String time;
    private String fromStopId;
    private String toStopId;
    private String description;
    private String other; 
}
