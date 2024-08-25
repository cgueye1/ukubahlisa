package com.wakana.samater.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wakana.samater.enums.NearbyFacilitiesEnum;
import com.wakana.samater.model.NearbyFacility;

public interface NearbyFacilityRepository  extends JpaRepository< NearbyFacility , Long> {

    List<NearbyFacility> findByLinkedStopIdAndNearbyFacilityType(String stopId, NearbyFacilitiesEnum facilityType);

}
