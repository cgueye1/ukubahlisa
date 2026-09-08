package com.wakana.realestateworks.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class DevisRequest {
    private MultipartFile file;
    private Long id;

}
