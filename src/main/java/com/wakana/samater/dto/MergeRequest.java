package com.wakana.samater.dto;
import java.util.List;

import lombok.Data;

@Data
public class MergeRequest {
    private   List<String> mp3FileUrl;
    private   List<String> mediaFileUrl;
    private Long voiceLangueCode;
    private String apiKey;
    private String clientId;
    private String telephone;

}
