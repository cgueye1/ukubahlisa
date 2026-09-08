package com.wakana.realestateworks.dto;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PaymentCallRequest {

    private String label;

    private double percentage;
    
    private double amount;
    
    

    private String expectedDate;


    private String status;

  
    private long realEstatePropertyId;
    
     private MultipartFile file;
     private String fileName;
    
}
