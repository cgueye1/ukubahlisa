package com.wakana.samater.services;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wakana.samater.dto.StopRequest;
import com.wakana.samater.model.Shop;
import com.wakana.samater.model.Stop;

public interface StopService {
     
     Stop saveStop(StopRequest stopRequest,String singlePicture, List<String> pictures ) ;
     Stop updateStop(StopRequest stopRequest,String idStop,String singlePicture, List<String> pictures ) ;
     Page<Stop> findAllByOrderByStopCodeAsc(Pageable pageable);
     Page<Stop> findByStopNameOrderByStopCodeAsc(String stopName, Pageable pageable);
     Stop findById(String id ) ;
     Stop removePicture( String picture, String idStop ) ;
     
     Stop removeShop( Long idShop, String idStop ) ;
     Stop addShop( Shop shopRequest, String idStop ) ;
     
}
