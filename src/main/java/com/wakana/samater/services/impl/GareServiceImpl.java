package com.wakana.samater.services.impl;
import java.util.List;
import org.springframework.stereotype.Service;
import com.wakana.samater.dto.GareRequest;
import com.wakana.samater.model.Gare;
import com.wakana.samater.repository.GareRepository;
import com.wakana.samater.services.GareService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GareServiceImpl implements GareService{  
    private final GareRepository gareRepository;
    @Override
    public List<Gare> getAllGars() {  
        return gareRepository.findAll();
    }
    @Override
    public Gare saveGare(GareRequest gareRequest) {
        Gare gare=new Gare();
        gare.setLatitude(gareRequest.getLatitude() );
        gare.setLongitude(gareRequest.getLongitude());
        gare.setLibelle(gareRequest.getLibelle());
        gare.setImg(gareRequest.getImg());
        return gareRepository.save(gare);
    }


    
     
     
    
}
