package com.wakana.samater.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.wakana.samater.dto.NearbyFacilityRequest;
import com.wakana.samater.model.NearbyFacility;
import com.wakana.samater.services.NearbyFacilityService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/nearbyfacility")
@RequiredArgsConstructor
public class NearbyFacilityController {
    
    @Value("${app.image.upload-url}") 
    private String uploadUrl;
    
    private final NearbyFacilityService nearbyFacilityService;
    
    @PostMapping("/save/{stopId}")
    public ResponseEntity<?> save(@PathVariable String stopId, @RequestBody NearbyFacilityRequest nearbyFacilityRequest) {
        try {
            NearbyFacility savedFacility = nearbyFacilityService.save(nearbyFacilityRequest,stopId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFacility);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the nearby facility.");
        }
    }
    
    @GetMapping("/all/{stopId}/{facilityType}")
    public ResponseEntity<?> getAllNearbyFacilities(@PathVariable String stopId, @PathVariable String facilityType) {
        try {

            List<NearbyFacility> facilities = nearbyFacilityService.getAllNearbyFacilies(stopId, facilityType);
            return ResponseEntity.ok(facilities);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving the nearby facilities.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNearbyFacilityById(@PathVariable Long id) {
        try {
            NearbyFacility facility = nearbyFacilityService.findById(id);
            if (facility == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nearby Facility not found.");
            }
            return ResponseEntity.ok(facility);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving the nearby facility.");
        }
        
        
     

    }
    
    
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody NearbyFacilityRequest nearbyFacilityRequest) {
        try {
            NearbyFacility updatedFacility = nearbyFacilityService.update(id, nearbyFacilityRequest);
            if (updatedFacility == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nearby Facility not found.");
            }
            return ResponseEntity.ok(updatedFacility);
        } catch (IllegalArgumentException e) {
            // Return bad request status if the facility type is invalid
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Handle other possible exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the nearby facility.");
        }
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletShop(@PathVariable Long id) {
        nearbyFacilityService.removeNearbyFacility(id);;
        return ResponseEntity.ok("deleted");

    }
}
