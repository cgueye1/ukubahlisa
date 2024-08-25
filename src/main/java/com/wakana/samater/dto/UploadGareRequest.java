package com.wakana.samater.dto;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UploadGareRequest {
   private MultipartFile file;
   private String libelle;
   private String img;
   private double latitude;
   private double longitude;


}
