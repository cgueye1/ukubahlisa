package com.wakana.realestateworks.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class MeetRequest {
    private String dateTime;
    private int duration;
    private List<Long> participants;
    private long propertyId;
    private long userId;
    private String title;
    private String address;
    private String description;
    private List<MultipartFile> files;

}
