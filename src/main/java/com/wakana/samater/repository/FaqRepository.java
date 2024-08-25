package com.wakana.samater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.wakana.samater.model.Faq;

public interface FaqRepository extends JpaRepository<Faq, Long> {

    Page<Faq> findAllByOrderByTimestampDesc(Pageable pageable);
    
    Page<Faq> findByTitleContainingOrDescriptionContainingOrderByTimestampDesc(String title, String description, Pageable pageable);
}
