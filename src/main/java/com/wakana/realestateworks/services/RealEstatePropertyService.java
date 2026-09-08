package com.wakana.realestateworks.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wakana.realestateworks.dto.ConstructionStatusKpiDto;
import com.wakana.realestateworks.dto.DashboardStats;
import com.wakana.realestateworks.dto.RealEstatePropertyRequest;
import com.wakana.realestateworks.dto.RealEstatePropertyResponse;
import com.wakana.realestateworks.dto.RealEstatePropertySearchRequest;
import com.wakana.realestateworks.dto.ReservationStats;
import com.wakana.realestateworks.enums.ConstructionStatusEnum;
import com.wakana.realestateworks.enums.ProfilEnum;
import com.wakana.realestateworks.enums.RealEstatePropertyStatusEnum;
import com.wakana.realestateworks.model.RealEstateProperty;
import com.wakana.realestateworks.model.User;

public interface RealEstatePropertyService {

        RealEstateProperty save(RealEstatePropertyRequest request, String planFileName, String legalStatusFileName,
                        List<String> pictureUrls);

        RealEstateProperty update(Long id, RealEstatePropertyRequest request, String planFileName,
                        String legalStatusFileName, List<String> pictureUrls);

        void delete(Long id);
        
          RealEstatePropertyResponse getProperty(Long propertyId);

        Page<RealEstatePropertyResponse> searchByPromoterAndParentIsNull(Long promoterId,String name, Pageable pageable);





}
