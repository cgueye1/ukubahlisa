package com.wakana.realestateworks.dto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentLotDto {
    private Long id;
    private String text;
    private Long userId;
    private String username;
    private Long lotId;
    private LocalDateTime createdAt;
}
