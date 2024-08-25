package com.wakana.samater.services;
import java.util.List;

import com.wakana.samater.dto.CategorieRequest;
import com.wakana.samater.model.Categorie;


public interface CategorieService {
     List<Categorie> getAllCategorie() ;
     List<Categorie> getCategoriesByIdUser(Long id) ;
     Categorie saveCategorie(CategorieRequest categorieRequest ) ;
    // List<Categorie> findCategorieByLibelle(String libelle);
    
}
