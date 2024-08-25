package com.wakana.samater.services.impl;
import java.util.List;
import org.springframework.stereotype.Service;
import com.wakana.samater.dto.ZoneRequest;
import com.wakana.samater.model.Gare;
import com.wakana.samater.model.Zone;
import com.wakana.samater.repository.GareRepository;
import com.wakana.samater.repository.ZoneRepository;
import com.wakana.samater.services.ZoneService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ZoneServiceImpl implements ZoneService{  
    private final ZoneRepository zoneRepository;
       private final GareRepository gareRepository;


    @Override
    public List<Zone> getAllZone() {
      return zoneRepository.findAll();
    }

    @Override
    public Zone saveZone(ZoneRequest zoneRequest) {
      Zone zone = new Zone();
      zone.setLibelle(zoneRequest.getLibelle());
      zone.setPrix_ticket_first(zoneRequest.getPrix_ticket_first());
      zone.setPrix_ticket_second(zoneRequest.getPrix_ticket_second());
      zone.setPrix_ticket_finzone(zoneRequest.getPrix_ticket_finzone());
      zone.setPrix_abo_hebdo_first(zoneRequest.getPrix_abo_hebdo_first());
      zone.setPrix_abo_hebdo_second(zoneRequest.getPrix_abo_hebdo_second());
      zone.setPrix_abo_mensuel_first(zoneRequest.getPrix_abo_mensuel_first());
      zone.setPrix_abo_mensuel_second(zoneRequest.getPrix_abo_mensuel_second());
      zone.setPrix_abo_hebdo_10_22(zoneRequest.getPrix_abo_hebdo_10_22());
      zone.setPrix_abo_mensuel_10_22(zoneRequest.getPrix_abo_mensuel_10_22());
      zone.setPrix_abo_hebdo_moins_10(zoneRequest.getPrix_abo_hebdo_moins_10());
      zone.setPrix_abo_mensuel_moins_10(zoneRequest.getPrix_abo_mensuel_moins_10());
      return zoneRepository.save(zone);
     
    }

    @Override
    public  Gare  addOneGare(Long idGare,Long idZone) {
    Gare gare = gareRepository.findById(idGare).orElse(null);
    Zone zone = zoneRepository.findById(idZone).orElse(null);
    if(null == gare){
      return null;
    } 
    
    gare.setZone(zone);
    return gareRepository.save(gare);
    
    
      
    
    }

    @Override
    public Zone updateZone(ZoneRequest zoneRequest, Long id) {
      Zone zone = zoneRepository.findById(id).orElse(null);
      zone.setPrix_ticket_first(zoneRequest.getPrix_ticket_first());
      zone.setPrix_ticket_second(zoneRequest.getPrix_ticket_second());
      zone.setPrix_ticket_finzone(zoneRequest.getPrix_ticket_finzone());
      zone.setPrix_abo_hebdo_first(zoneRequest.getPrix_abo_hebdo_first());
      zone.setPrix_abo_hebdo_second(zoneRequest.getPrix_abo_hebdo_second());
      zone.setPrix_abo_mensuel_first(zoneRequest.getPrix_abo_mensuel_first());
      zone.setPrix_abo_mensuel_second(zoneRequest.getPrix_abo_mensuel_second());
      zone.setPrix_abo_hebdo_10_22(zoneRequest.getPrix_abo_hebdo_10_22());
      zone.setPrix_abo_mensuel_10_22(zoneRequest.getPrix_abo_mensuel_10_22());
      zone.setPrix_abo_hebdo_moins_10(zoneRequest.getPrix_abo_hebdo_moins_10());
      zone.setPrix_abo_mensuel_moins_10(zoneRequest.getPrix_abo_mensuel_moins_10());  
      return zoneRepository.save(zone);
    }

    
    
     
     
    
}
