package com.wakana.samater.services.impl;
import java.util.List;
import org.springframework.stereotype.Service;
import com.wakana.samater.dto.GareRequest;
import com.wakana.samater.dto.VodRequest;
import com.wakana.samater.model.Gare;
import com.wakana.samater.model.Vod;
import com.wakana.samater.repository.GareRepository;
import com.wakana.samater.repository.VodRepository;
import com.wakana.samater.services.GareService;
import com.wakana.samater.services.VodService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VodServiceImpl implements VodService{  
    private final VodRepository vodRepository;

    @Override
    public List<Vod> getAllVideos() {
      return vodRepository.findAll();
    }

    @Override
    public Vod saveVideo(VodRequest vodRequest) {
        Vod vod = new Vod();
        vod.setDate(vodRequest.getDate());
        vod.setLink(vodRequest.getLink());
        vod.setTitle(vodRequest.getTitle());
        return vodRepository.save(vod);
    }
   


    
     
     
    
}
