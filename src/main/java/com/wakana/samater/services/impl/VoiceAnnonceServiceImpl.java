package com.wakana.samater.services.impl;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.wakana.samater.dto.VoiceAnnonceRequest;
import com.wakana.samater.model.VoiceAnnonce;
import com.wakana.samater.repository.VoiceAnnonceRepository;
import com.wakana.samater.services.VoiceAnnonceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoiceAnnonceServiceImpl implements VoiceAnnonceService{  
    private final VoiceAnnonceRepository voiceAnnonceRepository;


    public List<VoiceAnnonce> getAnnonce() {
       return  voiceAnnonceRepository.findAll();
    }



    public VoiceAnnonce saveAnnonce(VoiceAnnonceRequest voiceAnnonceRequest) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Africa/Dakar"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        long timestamp = now.toEpochSecond();
        String formattedDateTime = now.format(formatter);
        VoiceAnnonce voiceAnnonce = new VoiceAnnonce();
        voiceAnnonce.setAudio(voiceAnnonceRequest.getAudio());
        voiceAnnonce.setDate(formattedDateTime );
        voiceAnnonce.setTitle(voiceAnnonceRequest.getTitle());
        voiceAnnonce.setTimestamp(timestamp);
        voiceAnnonce.setAlert(voiceAnnonceRequest.isAlert());
        voiceAnnonce.setDescr(voiceAnnonceRequest.getDescr());
        return voiceAnnonceRepository.save(voiceAnnonce);
    }






    @Override
    public VoiceAnnonce getLastAnnonce() {
      return voiceAnnonceRepository.findFirstByOrderByTimestampDesc();
    }



    @Override
    public VoiceAnnonce getVoiceAnnonce(Long id) {
      return voiceAnnonceRepository.findById(id).orElse(null);
    }



    @Override
    public VoiceAnnonce updateAnnonce(VoiceAnnonceRequest voiceAnnonceRequest, Long id) {
      
        Optional<VoiceAnnonce> optionalVoice = voiceAnnonceRepository.findById(id);
        if (!optionalVoice .isPresent()) {
            return null; 
        }
        VoiceAnnonce voiceAnnonce= optionalVoice .get();
        voiceAnnonce.setTitle(voiceAnnonceRequest.getTitle());
        voiceAnnonce.setAlert(voiceAnnonceRequest.isAlert());
        voiceAnnonce.setDescr(voiceAnnonceRequest.getDescr());
        if(voiceAnnonceRequest.getAudio()!= null && !voiceAnnonceRequest.getAudio().isEmpty()){
          voiceAnnonce.setAudio(voiceAnnonceRequest.getAudio());     
        }
        return voiceAnnonceRepository.save(voiceAnnonce);
    }



    @Override
    public VoiceAnnonce publish(Long id) {
      ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Africa/Dakar"));
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
      long timestamp = now.toEpochSecond();
      String formattedDateTime = now.format(formatter);
      Optional<VoiceAnnonce> optionalVoice = voiceAnnonceRepository.findById(id);
      if (!optionalVoice .isPresent()) {
          return null; 
      }
      VoiceAnnonce voiceAnnonce= optionalVoice .get();
      voiceAnnonce.setDate(formattedDateTime );
      voiceAnnonce.setTimestamp(timestamp);
      return voiceAnnonceRepository.save(voiceAnnonce);
    }



    @Override
    public Page<VoiceAnnonce> findAllByOrderByTimestampDesc(Pageable pageable) {
     
     return voiceAnnonceRepository.findAllByOrderByTimestampDesc(pageable);
    }



    @Override
    public Page<VoiceAnnonce> findByTitleContainingOrDescrContainingOrderByTimestampDesc(String title, String descr,
        Pageable pageable) {


     return voiceAnnonceRepository.findByTitleContainingOrDescrContainingOrderByTimestampDesc(title,descr,pageable);
    }


   


  

  


    
     
     
    
}
