package com.wakana.samater.services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.wakana.samater.dto.ActuRequest;
import com.wakana.samater.model.BgImage;

public interface BgImageService {
    BgImage saveBgImage(ActuRequest actuRequest ) ;
    BgImage findFirstByOrderByDateDesc(); 

    
    
    //Page<Actu> findAllByOrderByDateDesc(Pageable pageable);
  //  Page<Actu> findByTitleContainingOrDescriptionContainingOrderByDateDesc(String title, String description, Pageable pageable);
    
    
    
}
