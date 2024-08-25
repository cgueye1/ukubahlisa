package com.wakana.samater.dto;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ShopRequest {
    private MultipartFile file;
    private String libelle;
    private String descr;
    private String picture;
    

}
