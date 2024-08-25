package com.wakana.samater.services;
import java.util.List;

import com.wakana.samater.dto.NearbyFacilityRequest;
import com.wakana.samater.model.NearbyFacility;

public interface NearbyFacilityService {
      List<NearbyFacility> getAllNearbyFacilies(String stopId, String facilityType) ;
      NearbyFacility save(NearbyFacilityRequest NearbyFacilityRequest,String idStop ) ;
      NearbyFacility findById(Long id) ;
      
      void removeNearbyFacility(Long id) ;
      NearbyFacility update(Long id, NearbyFacilityRequest nearbyFacilityRequest);


    
}
