package com.wakana.realestateworks.dto;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PartnerRequest {
    private String name;
     private MultipartFile logoFile;
    private String link;
}
