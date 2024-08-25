package com.wakana.samater.services.impl;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wakana.samater.dto.NearbyFacilityRequest;
import com.wakana.samater.enums.NearbyFacilitiesEnum;
import com.wakana.samater.model.NearbyFacility;
import com.wakana.samater.model.Shop;
import com.wakana.samater.model.Stop;
import com.wakana.samater.repository.NearbyFacilityRepository;
import com.wakana.samater.repository.StopRepository;
import com.wakana.samater.services.NearbyFacilityService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NearbyFacilityServiceImpl implements NearbyFacilityService {
    
    private final NearbyFacilityRepository nearbyFacilityRepository;
    private final StopRepository stopRepository;
    @Override
    public List<NearbyFacility> getAllNearbyFacilies(String stopId, String facilityType) {
        NearbyFacilitiesEnum facilityEnum;
        try {
            facilityEnum = NearbyFacilitiesEnum.valueOf(facilityType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid nearby facility type: " + facilityType);
        }

        return nearbyFacilityRepository.findByLinkedStopIdAndNearbyFacilityType(stopId, facilityEnum);
    }

    @Override
    @Transactional
    public NearbyFacility save(NearbyFacilityRequest nearbyFacilityRequest,String idStop) {
        NearbyFacility nearbyFacility = new NearbyFacility();
        nearbyFacility.setDescr(nearbyFacilityRequest.getDescr());
        nearbyFacility.setLat(nearbyFacilityRequest.getLat());
        nearbyFacility.setLon(nearbyFacilityRequest.getLon());
        nearbyFacility.setLibelle(nearbyFacilityRequest.getLibelle());

        NearbyFacilitiesEnum facilityEnum;
        try {
            facilityEnum = NearbyFacilitiesEnum.valueOf(nearbyFacilityRequest.getNearbyFacilityType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid nearby facility type: " + nearbyFacilityRequest.getNearbyFacilityType());
        }
        nearbyFacility.setNearbyFacilityType(facilityEnum);
          Optional<Stop> optionalStop = stopRepository.findById(idStop);
        if (!optionalStop.isPresent()) {
            return null; 
        }

        Stop stop = optionalStop.get();
        nearbyFacility.setLinkedStop(stop);

        NearbyFacility savedFacility = nearbyFacilityRepository.save(nearbyFacility);

        return  savedFacility;
    }

    @Override
    public NearbyFacility findById(Long id) {
 
      return nearbyFacilityRepository.findById(id).orElse(null);
    }
    
    
    
    @Override
    @Transactional
    public NearbyFacility update(Long id, NearbyFacilityRequest nearbyFacilityRequest) {
        NearbyFacility existingFacility = nearbyFacilityRepository.findById(id).orElse(null);
        if (existingFacility == null) {
            return null;
        }

        existingFacility.setDescr(nearbyFacilityRequest.getDescr());
        existingFacility.setLat(nearbyFacilityRequest.getLat());
        existingFacility.setLon(nearbyFacilityRequest.getLon());
        existingFacility.setLibelle(nearbyFacilityRequest.getLibelle());

        NearbyFacilitiesEnum facilityEnum;
        try {
            facilityEnum = NearbyFacilitiesEnum.valueOf(nearbyFacilityRequest.getNearbyFacilityType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid nearby facility type: " + nearbyFacilityRequest.getNearbyFacilityType());
        }
        existingFacility.setNearbyFacilityType(facilityEnum);

        return nearbyFacilityRepository.save(existingFacility);
    }

    @Override
    public void removeNearbyFacility(Long id) {
       Optional< NearbyFacility> optionalNearbyFacility = nearbyFacilityRepository.findById(id);
        if (optionalNearbyFacility.isPresent()) {
            nearbyFacilityRepository.deleteById(id);
        } else {
        }
    }
}
