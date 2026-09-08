package com.wakana.realestateworks.dto;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class RapportRequest {
  
    private String titre;
    private String description;
    private Long propertyId;
    private MultipartFile file;
  
}
