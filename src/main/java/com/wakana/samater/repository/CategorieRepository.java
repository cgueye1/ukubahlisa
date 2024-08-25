package com.wakana.samater.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.wakana.samater.model.Categorie;

import java.util.List;


public interface CategorieRepository  extends JpaRepository< Categorie , Long> {

List<Categorie> findByIdUser(Long idUser);
    
}
