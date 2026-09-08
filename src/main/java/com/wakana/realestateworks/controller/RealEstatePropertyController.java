package com.wakana.realestateworks.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wakana.realestateworks.dto.RealEstatePropertyRequest;
import com.wakana.realestateworks.dto.RealEstatePropertySearchRequest;

import com.wakana.realestateworks.enums.ConstructionStatusEnum;
import com.wakana.realestateworks.model.RealEstateProperty;
import com.wakana.realestateworks.services.RealEstatePropertyService;
import com.wakana.realestateworks.util.FileTransferUtil;

import io.swagger.v3.oas.annotations.Hidden;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/realestate")
@RequiredArgsConstructor
public class RealEstatePropertyController {

    private final RealEstatePropertyService realEstatePropertyService;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(@ModelAttribute RealEstatePropertyRequest request) {
        try {

            String planFileName = FileTransferUtil.handleFileUpload(request.getPlan());

            String legalStatusFileName = FileTransferUtil.handleFileUpload(request.getLegalStatus());

            List<String> pictureUrls = FileTransferUtil.uploadPictures(request.getPictures());

            RealEstateProperty savedProperty = realEstatePropertyService.save(
                    request, planFileName, legalStatusFileName, pictureUrls);

            return ResponseEntity.ok(Collections.singletonMap("realEstateProperty", "Saved"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Échec d'enregistrement : " + e.getMessage());
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id, @ModelAttribute RealEstatePropertyRequest request) {
        try {

            String planFileName = FileTransferUtil.handleFileUpload(request.getPlan());
            String legalStatusFileName = FileTransferUtil.handleFileUpload(request.getLegalStatus());
            List<String> pictureUrls = FileTransferUtil.uploadPictures(request.getPictures());
            RealEstateProperty updatedProperty = realEstatePropertyService.update(
                    id, request, planFileName, legalStatusFileName, pictureUrls);
            return ResponseEntity.ok(Collections.singletonMap("realEstateProperty", updatedProperty));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Échec de la mise à jour : " + e.getMessage());
        }
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> details(@PathVariable Long id) {
        try {

            return ResponseEntity
                    .ok(Collections.singletonMap("realEstateProperty", realEstatePropertyService.getProperty(id)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status value");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating status: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            realEstatePropertyService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Échec de la suppression : " + e.getMessage());
        }
    }

    @PostMapping("/search-by-promoter")
    public ResponseEntity<Page<?>> searchByPromoter(
            @RequestBody RealEstatePropertySearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<?> realEstatePropertyPage = realEstatePropertyService.searchByPromoterAndParentIsNull(
                request.getPromoterId(),
                request.getName(),
                pageable);

        long totalElements = realEstatePropertyPage.getTotalElements();
        int totalPages = realEstatePropertyPage.getTotalPages();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("X-Total-Elements", String.valueOf(totalElements));
        responseHeaders.add("X-Total-Pages", String.valueOf(totalPages));

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(realEstatePropertyPage);
    }

}
