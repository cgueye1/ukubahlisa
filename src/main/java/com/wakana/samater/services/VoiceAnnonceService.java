package com.wakana.samater.services;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wakana.samater.dto.VoiceAnnonceRequest;
import com.wakana.samater.model.VoiceAnnonce;


public interface VoiceAnnonceService {
   List< VoiceAnnonce> getAnnonce() ;
   VoiceAnnonce saveAnnonce(VoiceAnnonceRequest voiceAnnonceRequest ) ;
   VoiceAnnonce updateAnnonce(VoiceAnnonceRequest voiceAnnonceRequest,Long id ) ;
   VoiceAnnonce getVoiceAnnonce(Long id) ;

   VoiceAnnonce publish(Long id) ;
   VoiceAnnonce getLastAnnonce();
   
   
     Page<VoiceAnnonce> findAllByOrderByTimestampDesc(Pageable pageable);
     Page< VoiceAnnonce> findByTitleContainingOrDescrContainingOrderByTimestampDesc(String title, String descr, Pageable pageable);
    
}
