package com.wakana.realestateworks.services;

import com.wakana.realestateworks.dto.PointingAddressRequest;
import com.wakana.realestateworks.model.PointingAddress;

import java.util.List;

public interface PointingAddressService {
    PointingAddress save(PointingAddressRequest pointingAddress);

    PointingAddress update(Long id, PointingAddressRequest pointingAddress);

    void delete(Long id);

    PointingAddress findById(Long id);

    List<PointingAddress> findAll();

    List<PointingAddress> getPointingAddressesByProperty(Long propertyId);

}
