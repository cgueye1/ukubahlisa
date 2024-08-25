package com.wakana.samater.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wakana.samater.dto.CarteRequest;
import com.wakana.samater.services.CarteService;

import lombok.RequiredArgsConstructor;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/carte")
@RequiredArgsConstructor
public class CarteController {
    private final CarteService carteService;
    
    @PostMapping("/save")
    public ResponseEntity<?>   saveCarte(@RequestBody CarteRequest carteRequest) {
        return ResponseEntity.ok(carteService.saveCarte(carteRequest));
        
    }
    
    @GetMapping("/user/all/{id:.+}")
     public ResponseEntity<?>   getAllCarte(@PathVariable Long id) {
        return ResponseEntity.ok(carteService.getAllCarteByUser(id));
        
    }
    @PutMapping("/abonner/{id:.+}")
     public ResponseEntity<?>   abonner(@RequestBody CarteRequest carteRequest, @PathVariable Long id) {
        return ResponseEntity.ok(carteService.abonnement(carteRequest, id));
        
    }
    @GetMapping("/user/histories/all/{id:.+}")
    public ResponseEntity<?>   getAllCarteHistories(@PathVariable Long id) {
       return ResponseEntity.ok(carteService.getAllCarteHistoriesByUser(id));
       
   }
   
     
   

    
}
