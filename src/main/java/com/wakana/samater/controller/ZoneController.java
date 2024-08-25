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
import com.wakana.samater.dto.ZoneRequest;
import com.wakana.samater.services.ZoneService;
import lombok.RequiredArgsConstructor;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/zone")
@RequiredArgsConstructor
public class ZoneController {
    private final ZoneService zoneService;
    
    @PostMapping("/save")
    public ResponseEntity<?>   saveZone(@RequestBody ZoneRequest zoneRequest) {
        return ResponseEntity.ok(zoneService.saveZone(zoneRequest));
        
    }
    
    @GetMapping("/all")
     public ResponseEntity<?>   getAllZone() {
        return ResponseEntity.ok(zoneService.getAllZone());
        
    }
   
    @PutMapping("/addgare/{idgare:.+}/{idzone:.+}")
     public ResponseEntity<?>   addGare(@PathVariable Long idgare,@PathVariable Long idzone) {
          return ResponseEntity.ok(zoneService.addOneGare(idgare, idzone));
        
    }
     
    @PutMapping("/update/prix/{id:.+}")
    public ResponseEntity<?>   updatePrix(@RequestBody ZoneRequest zoneRequest,@PathVariable Long id) {
         return ResponseEntity.ok(zoneService.updateZone(zoneRequest, id));
       
   }
   

    
}
