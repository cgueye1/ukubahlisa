package com.wakana.samater.services.impl;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import com.wakana.samater.dto.ActuRequest;
import com.wakana.samater.model.BgImage;
import com.wakana.samater.repository.BgImageRepository;
import com.wakana.samater.services.BgImageService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BgImageServiceImpl implements  BgImageService{  
    private final BgImageRepository bgImageRepository;

    @Override
    public BgImage saveBgImage(ActuRequest actuRequest) {
      ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Africa/Dakar"));
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
      long timestamp = now.toEpochSecond();
      String formattedDateTime = now.format(formatter);
      BgImage bgImage = new BgImage();
      bgImage.setDate(now.toString());
      bgImage.setDescription(actuRequest.getDescription());
      bgImage.setImg(actuRequest.getImg());
      bgImage.setTitle(actuRequest.getTitle());
      bgImage.setLink(actuRequest.getLink());
      return bgImageRepository.save(bgImage);
    }

    @Override
    public BgImage findFirstByOrderByDateDesc() {
     return bgImageRepository.findFirstByOrderByDateDesc();
    }

 
  

  


    
     
     
    
}
