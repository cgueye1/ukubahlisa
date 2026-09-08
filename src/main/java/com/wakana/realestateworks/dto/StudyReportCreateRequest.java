package com.wakana.realestateworks.dto;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StudyReportCreateRequest {
    private String title;
    private MultipartFile file;
    @Schema(hidden = true)
    private String fileUrl;
    private int versionNumber;
    private Long studyRequestId;
    private Long authorId; // BET
}