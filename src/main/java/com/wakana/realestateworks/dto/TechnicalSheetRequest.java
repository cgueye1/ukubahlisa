package com.wakana.realestateworks.dto;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class TechnicalSheetRequest {
    private MultipartFile file;
    private long id;
    private String sheetName;
 
}
