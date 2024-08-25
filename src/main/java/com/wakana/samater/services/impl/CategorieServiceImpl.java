package com.wakana.samater.services.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wakana.samater.dto.CategorieRequest;
import com.wakana.samater.model.Categorie;
import com.wakana.samater.repository.CategorieRepository;
import com.wakana.samater.services.CategorieService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategorieServiceImpl implements CategorieService{
     

  
    private final CategorieRepository categorieRepository;

 
    public List<Categorie> getAllCategorie() {
         return categorieRepository.findAll();
    }

  
    public Categorie saveCategorie(CategorieRequest categorieRequest) {
        Categorie categorie = new Categorie();
        categorie.setLibelle(categorieRequest.getLibelle());
        categorie.setIdUser(categorieRequest.getIdUser());
        return categorieRepository.save(categorie);
    }


    public List<Categorie> getCategoriesByIdUser(Long id) {
         return categorieRepository.findByIdUser(id);
    }

  
  
     
     
    
}
