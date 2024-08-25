package com.wakana.samater.services;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wakana.samater.dto.ActuRequest;
import com.wakana.samater.model.Actu;

public interface ActuService {
     List< Actu> getAllActus() ;
    Actu saveActu(ActuRequest actuRequest ) ;
    Actu updateActu(ActuRequest actuRequest,Long id ) ;
    Actu getActu(Long id) ;
    
    
    Page<Actu> findAllByOrderByDateDesc(Pageable pageable);
    Page<Actu> findByTitleContainingOrDescriptionContainingOrderByDateDesc(String title, String description, Pageable pageable);
    
    
    
}
