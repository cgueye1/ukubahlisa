package com.wakana.samater.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;
import java.util.List;

@Data
public class StopRequest {

    private String stop_id;
    private String stop_code;
    private String stop_name;
    private String location_type;
    private String parent_station;
    private String platform_code;
    private String stop_timezone;
    private String stop_desc;
    private double stop_lat;
    private double stop_lon;
    private boolean terAgency;
    private boolean parking;
    private boolean toilets;
    private boolean shops;
    private boolean waitingRoom;
    private boolean lostAndFound;
    private boolean markets;
    private boolean schools;
    private boolean sitesToVisit;
    private boolean pharmacy;
    private MultipartFile picture;
    private List<MultipartFile> pictures;
}
