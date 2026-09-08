package com.wakana.realestateworks.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
public class ProgressAlbumRequest {
    private Long realEstatePropertyId;
    private String name;
    private String description;
    private List<MultipartFile> pictures;
    @Schema(hidden = true)
    private boolean entrance;
}
