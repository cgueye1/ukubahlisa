package com.wakana.samater.services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wakana.samater.dto.FaqRequest;
import com.wakana.samater.model.Faq;

public interface FaqService {
    
    Faq saveFaq(FaqRequest faqRequest ) ;
    Faq updateFaq(FaqRequest faqRequest,Long id ) ;
    Faq getFaq(Long id ) ;
    void removeFaq(Long id) ;
    
    Page<Faq> findAllByOrderByDateDesc(Pageable pageable);
    
    Page<Faq> findByTitleContainingOrDescriptionContainingOrderByDateDesc(String title, String description, Pageable pageable);
 
}
