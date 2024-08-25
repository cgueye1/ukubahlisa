package com.wakana.samater.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wakana.samater.dto.ZoneIntervalleRequest;

import com.wakana.samater.services.ZoneIntervalleService;


import lombok.RequiredArgsConstructor;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/zoneinterval")
@RequiredArgsConstructor
public class ZoneIntervalleController {
    private final ZoneIntervalleService zoneService;
    
    @PostMapping("/save")
    public ResponseEntity<?>   saveZoneInter(@RequestBody ZoneIntervalleRequest zoneIntervalleRequest) {
        return ResponseEntity.ok(zoneService.saveZoneIntervalle(zoneIntervalleRequest));
        
    }
    
    @GetMapping("/all")
     public ResponseEntity<?>   getAllZoneInter() {
        return ResponseEntity.ok(zoneService.getAllZoneIntervalle());
        
    }
   
     
   

    
}
