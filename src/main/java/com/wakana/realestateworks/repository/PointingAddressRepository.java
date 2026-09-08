package com.wakana.realestateworks.repository;

import com.wakana.realestateworks.model.PointingAddress;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointingAddressRepository extends JpaRepository<PointingAddress, Long> {
    List<PointingAddress> findByRealEstatePropertyId(Long propertyId);

}
