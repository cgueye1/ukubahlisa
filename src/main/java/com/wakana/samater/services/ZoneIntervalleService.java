package com.wakana.samater.services;
import java.util.List;
import com.wakana.samater.dto.ZoneIntervalleRequest;
import com.wakana.samater.model.ZoneIntervalle;


public interface ZoneIntervalleService {
     List<ZoneIntervalle> getAllZoneIntervalle() ;
     ZoneIntervalle saveZoneIntervalle(ZoneIntervalleRequest zoneIntervalleRequest ) ;
    
}
