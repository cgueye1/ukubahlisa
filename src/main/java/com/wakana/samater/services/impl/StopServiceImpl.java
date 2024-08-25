package com.wakana.samater.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wakana.samater.dto.StopRequest;
import com.wakana.samater.model.Shop;
import com.wakana.samater.model.Stop;
import com.wakana.samater.repository.ShopRepository;
import com.wakana.samater.repository.StopRepository;
import com.wakana.samater.services.StopService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StopServiceImpl implements StopService {

    private final StopRepository stopRepository;
    private final ShopRepository shopRepository;
    

    @Override
    @Transactional
    public Stop saveStop(StopRequest stopRequest, String singlePicture, List<String> pictures) {
        Stop stop = new Stop();
        stop.setId(stopRequest.getStop_id());
        stop.setLocationType(stopRequest.getLocation_type());
        stop.setLostAndFound(stopRequest.isLostAndFound());
        stop.setParentStation(stopRequest.getParent_station());
        stop.setParking(stopRequest.isParking());
        stop.setPlatformCode(stopRequest.getPlatform_code());
        stop.setShops(stopRequest.isShops());
        stop.setStopCode(stopRequest.getStop_code());
        stop.setStopDesc(stopRequest.getStop_desc());
        stop.setStopLat(stopRequest.getStop_lat());
        stop.setStopLon(stopRequest.getStop_lon());
        stop.setStopName(stopRequest.getStop_name());
        stop.setStopTimezone(stopRequest.getStop_timezone());
        stop.setTerAgency(stopRequest.isTerAgency());
        stop.setToilets(stopRequest.isToilets());
        stop.setWaitingRoom(stopRequest.isWaitingRoom());
        stop.setPictures(pictures);

        if (singlePicture != null && !singlePicture.isEmpty()) {
            stop.setPicture(singlePicture);
        }
      
      
    
        return stopRepository.save(stop);
    }

    @Override
    @Transactional
    public Stop updateStop(StopRequest stopRequest, String idStop, String singlePicture, List<String> pictures) {
        Optional<Stop> optionalStop = stopRepository.findById(idStop);
        if (!optionalStop.isPresent()) {
            return null; 
        }

        Stop stop = optionalStop.get();
        stop.setLocationType(stopRequest.getLocation_type());

        stop.setParentStation(stopRequest.getParent_station());
    
        stop.setPlatformCode(stopRequest.getPlatform_code());
        stop.setShops(stopRequest.isShops());
        stop.setStopCode(stopRequest.getStop_code());
        stop.setStopDesc(stopRequest.getStop_desc());
        stop.setStopLat(stopRequest.getStop_lat());
        stop.setStopLon(stopRequest.getStop_lon());
        stop.setStopName(stopRequest.getStop_name());
        stop.setStopTimezone(stopRequest.getStop_timezone());
        
        stop.setLostAndFound(stopRequest.isLostAndFound());
        stop.setParking(stopRequest.isParking());
        stop.setTerAgency(stopRequest.isTerAgency());
        stop.setToilets(stopRequest.isToilets());
        stop.setWaitingRoom(stopRequest.isWaitingRoom());
    

        if (singlePicture != null && !singlePicture.isEmpty()) {
            stop.setPicture(singlePicture);
        }
        
        
    if (pictures != null && !pictures.isEmpty()) {
        stop.getPictures().addAll(pictures);
    }


        return stopRepository.save(stop);
    }

    @Override
    public Page<Stop> findAllByOrderByStopCodeAsc(Pageable pageable) {
        return stopRepository.findAllByOrderByStopCodeAsc(pageable);
    }

    @Override
    public Page<Stop> findByStopNameOrderByStopCodeAsc(String stopName, Pageable pageable) {
        return stopRepository. findByStopNameContainingOrderByStopCodeAsc(stopName, pageable);
    }

    @Override
    public Stop findById(String id) {
        return stopRepository.findById(id).orElse(null);
    }

    @Override
    public Stop removePicture(String pictureFileName, String stopId) {
        Optional<Stop> optionalStop = stopRepository.findById(stopId);
        if (optionalStop.isEmpty()) {
            return null; 
        }
    
        Stop stop = optionalStop.get();
    
        if (stop.getPictures() != null && !stop.getPictures().isEmpty()) {
            boolean removed = stop.getPictures().remove(pictureFileName);
            if (!removed) {
                return null; 
            }
        }

        return   stopRepository.save(stop);
    }

    @Override
    public Stop removeShop(Long idShop, String idStop) {
        Optional<Shop> optionalShop = shopRepository.findById(idShop);
        if (optionalShop.isEmpty()) {
            return null; 
        }
        Shop shop = optionalShop.get();
    
        Optional<Stop> optionalStop = stopRepository.findById(idStop);
        if (optionalStop.isEmpty()) {
            return null;  
        }
    
        Stop stop = optionalStop.get();
    
        boolean removed = stop.getShopslist().remove(shop);
    
        if (!removed) {
            return null;  
        }
    
        return stopRepository.save(stop);
    }
    
    public Stop addShop(Shop shopRequest, String idStop) {
        Optional<Shop> optionalShop = shopRepository.findById(shopRequest.getId());
        if (optionalShop.isEmpty()) {
            return null; 
        }
        
        Shop shop = optionalShop.get();
        
        Optional<Stop> optionalStop = stopRepository.findById(idStop);
        if (optionalStop.isEmpty()) {
            return null;  
        }
        
        Stop stop = optionalStop.get();
        
        if (stop.getShopslist().contains(shop)) {
            return null; 
        }
        
        stop.getShopslist().add(shop);
        
        return stopRepository.save(stop);
    }
    
    
}
