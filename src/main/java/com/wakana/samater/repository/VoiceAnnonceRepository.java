package com.wakana.samater.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wakana.samater.model.VoiceAnnonce;


public interface VoiceAnnonceRepository  extends JpaRepository< VoiceAnnonce , Long> {
     VoiceAnnonce findFirstByOrderByTimestampDesc(); 
    
    
     Page<VoiceAnnonce> findAllByOrderByTimestampDesc(Pageable pageable);
    
     Page< VoiceAnnonce> findByTitleContainingOrDescrContainingOrderByTimestampDesc(String title, String descr, Pageable pageable);
    
}
