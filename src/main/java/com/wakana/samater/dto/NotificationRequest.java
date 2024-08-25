package com.wakana.samater.dto;

import lombok.Data;

@Data
public class NotificationRequest {

    private String date;
    private Long idUser;
    private String title;
    private String img;
    private String description;  
}
