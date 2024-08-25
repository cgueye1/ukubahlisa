package com.wakana.samater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.wakana.samater.model.LostThing;

public interface LostThingRepository extends JpaRepository<LostThing, Long> {

    Page<LostThing> findAllByOrderByDateDesc(Pageable pageable);
    
    Page<LostThing> findByFirstnameContainingOrLastnameContainingOrDescriptionContainingOrderByDateDesc(
        String firstname, String lastname, String description, Pageable pageable
    );
}
