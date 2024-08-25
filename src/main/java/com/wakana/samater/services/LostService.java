package com.wakana.samater.services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wakana.samater.dto.LostRequest;
import com.wakana.samater.model.LostThing;

public interface LostService {

    LostThing saveLost(LostRequest lostRequest ) ;
    LostThing getLost(Long id ) ;
    LostThing founded(Long id ) ;
    Page<LostThing> findAllByOrderByDateDesc(Pageable pageable);
    Page<LostThing> findByFirstnameContainingOrLastnameContainingOrDescriptionContainingOrderByDateDesc(
        String firstname, String lastname, String description, Pageable pageable
    );
    
    
    
}
