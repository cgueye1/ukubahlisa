package com.wakana.samater.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wakana.samater.dto.CategorieRequest;
import com.wakana.samater.dto.ExpressionRequest;
import com.wakana.samater.services.CategorieService;

import lombok.RequiredArgsConstructor;
@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/api/v1/categorie")
@RequiredArgsConstructor
public class CategorieController {
    private final  CategorieService categorieService;
    
    @PostMapping("/save")
    public ResponseEntity<?>   saveCategorie(@RequestBody CategorieRequest categorieRequest) {
        return ResponseEntity.ok(categorieService.saveCategorie(categorieRequest));
        
    }
    
    @GetMapping("/all")
     public ResponseEntity<?>   getAllCategorie() {
        return ResponseEntity.ok(categorieService.getAllCategorie());
        
    }
   
     
     @GetMapping("/user/{id:.+}")
     public ResponseEntity<?>   validOpp(@PathVariable Long id) {
        return ResponseEntity.ok(categorieService.getCategoriesByIdUser(id));
        
    }

    
}
