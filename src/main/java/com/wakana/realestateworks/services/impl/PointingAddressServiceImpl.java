package com.wakana.realestateworks.services.impl;

import com.wakana.realestateworks.dto.PointingAddressRequest;
import com.wakana.realestateworks.model.PointingAddress;
import com.wakana.realestateworks.model.RealEstateProperty;
import com.wakana.realestateworks.repository.PointingAddressRepository;
import com.wakana.realestateworks.repository.RealEstatePropertyRepository;
import com.wakana.realestateworks.services.PointingAddressService;
import com.wakana.realestateworks.util.QRCodeUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointingAddressServiceImpl implements PointingAddressService {

    private final PointingAddressRepository pointingAddressRepository;
    private final RealEstatePropertyRepository realEstatePropertyRepository;

    @Override
    public PointingAddress save(PointingAddressRequest request) {
        // 1. Décoder le QR code
        String[] qrData = QRCodeUtil.decodeQrCode(request.getQrcode());
        if (qrData == null || qrData.length == 0) {
            throw new IllegalArgumentException("QR Code invalide ou illisible.");
        }

        Long propertyId;
        try {
            propertyId = Long.parseLong(qrData[0]); // L'ID du property est en première position
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("QR Code invalide : ID de propriété incorrect.");
        }
        RealEstateProperty property = realEstatePropertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        PointingAddress pointingAddress = new PointingAddress();
        pointingAddress.setLatitude(request.getLatitude());
        pointingAddress.setLongitude(request.getLongitude());
        pointingAddress.setName(request.getName());
        pointingAddress.setRealEstateProperty(property);
        return pointingAddressRepository.save(pointingAddress);
    }

    @Override
    public PointingAddress update(Long id, PointingAddressRequest pointingAddress) {
        PointingAddress existing = pointingAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pointing address not found"));
        existing.setName(pointingAddress.getName());
        existing.setLatitude(pointingAddress.getLatitude());
        existing.setLongitude(pointingAddress.getLongitude());
        return pointingAddressRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        pointingAddressRepository.deleteById(id);
    }

    @Override
    public PointingAddress findById(Long id) {
        return pointingAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pointing address not found"));
    }

    @Override
    public List<PointingAddress> findAll() {
        return pointingAddressRepository.findAll();
    }

    @Override
    public List<PointingAddress> getPointingAddressesByProperty(Long propertyId) {
        return pointingAddressRepository.findByRealEstatePropertyId(propertyId);
    }
}
