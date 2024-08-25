package com.wakana.samater.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.wakana.samater.services.TerNotificationService;

import lombok.RequiredArgsConstructor;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/notif")
@RequiredArgsConstructor
public class TerNotifController {
    private final TerNotificationService terNotificationService;

    @GetMapping("/get/{id:.+}")
     public ResponseEntity<?>   getnotifs(@PathVariable Long id) {
        return ResponseEntity.ok(terNotificationService.findByidUser(id));
        
    }
    @PutMapping("/view/{id:.+}")
    public ResponseEntity<?>   viewNotif(@PathVariable Long id) {
       return ResponseEntity.ok(terNotificationService.view(id));
       
   }

    
}
