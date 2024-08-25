package com.wakana.samater.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ipn")
@RequiredArgsConstructor
public class IpnController {
    
    
    
    @GetMapping("/success")
     public ResponseEntity<?>   getSuccess() {
        return ResponseEntity.ok(true);
        
    }
     
    @GetMapping("/cancel")
    public ResponseEntity<?>   getCancel() {
       return ResponseEntity.ok(false);
       
   }
   
    
    
}
