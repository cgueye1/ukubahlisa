package com.wakana.samater.services;
import java.util.List;
import com.wakana.samater.dto.ZoneRequest;
import com.wakana.samater.model.Gare;
import com.wakana.samater.model.Zone;


public interface ZoneService {
     List< Zone> getAllZone() ;
     Zone saveZone(ZoneRequest zoneRequest ) ;
     Zone updateZone(ZoneRequest zoneRequest ,Long id) ;
     Gare  addOneGare(Long idGare,Long idZone) ;
    
}
