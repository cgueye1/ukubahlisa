package com.wakana.realestateworks.services.impl;

import org.springframework.stereotype.Service;

import com.wakana.realestateworks.dto.RealEstatePropertyRequest;
import com.wakana.realestateworks.dto.RealEstatePropertyResponse;

import com.wakana.realestateworks.enums.ConstructionStatusEnum;
import com.wakana.realestateworks.enums.ProfilEnum;
import com.wakana.realestateworks.enums.RealEstatePropertyStatusEnum;

import com.wakana.realestateworks.model.PropertyType;
import com.wakana.realestateworks.model.RealEstateProperty;

import com.wakana.realestateworks.model.Subscription;
import com.wakana.realestateworks.model.User;

import com.wakana.realestateworks.repository.PropertyTypeRepository;
import com.wakana.realestateworks.repository.RealEstatePropertyRepository;
import com.wakana.realestateworks.repository.UserRepository;

import com.wakana.realestateworks.services.EmailService;

import com.wakana.realestateworks.services.RealEstatePropertyService;
import com.wakana.realestateworks.services.SubscriptionService;
import com.wakana.realestateworks.util.FileTransferUtil;
import com.wakana.realestateworks.util.QRCodeUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

import java.util.List;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class RealEstatePropertyServiceImpl implements RealEstatePropertyService {
        private final RealEstatePropertyRepository realEstatePropertyRepository;
        private final UserRepository userRepository;
        private final PropertyTypeRepository propertyTypeRepository;

        @Autowired
        private EmailService emailService;

        private final SubscriptionService subscriptionService;

        @Override
        public RealEstateProperty save(RealEstatePropertyRequest request, String planFileName,
                        String legalStatusFileName,
                        List<String> pictureUrls) {
                RealEstateProperty realEstateProperty = new RealEstateProperty();

                ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Africa/Dakar"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

                long timestamp = now.toEpochSecond();

                realEstateProperty.setLotFeesPaid(false);
                realEstateProperty.setName(request.getName());
                realEstateProperty.setNumber(request.getNumber());
                realEstateProperty.setAddress(request.getAddress());
                realEstateProperty.setPrice(request.getPrice());
                realEstateProperty.setNumberOfRooms(request.getNumberOfRooms());
                realEstateProperty.setArea(request.getArea());
                realEstateProperty.setLatitude(request.getLatitude());
                realEstateProperty.setLongitude(request.getLongitude());
                realEstateProperty.setAvailable(request.isAvailable());
                realEstateProperty.setReservationFee(request.getReservationFee());
                realEstateProperty.setDescription(request.getDescription());
                realEstateProperty.setNumberOfLots(request.getNumberOfLots());
                realEstateProperty.setDiscount(request.getDiscount());
                realEstateProperty.setLevel(request.getLevel());
                realEstateProperty.setFeesFile(request.getFeesFile());
                realEstateProperty.setTimestamp(timestamp);
                // Gérer les champs booléens pour les équipements communs

                realEstateProperty.setStatus(RealEstatePropertyStatusEnum.AVAILABLE);

                realEstateProperty.setConstructionStatus(ConstructionStatusEnum.IN_PROGRESS);
                realEstateProperty.setCreatedAt(LocalDateTime.now());

                // Set promoter, recipient, and notary

                if (request.getPromoterId() != 0) {
                        User promoter = userRepository.findById(request.getPromoterId())
                                        .orElseThrow(() -> new RuntimeException("Promoter not found"));

                        if (ProfilEnum.PROMOTEUR.equals(promoter.getProfil())) {
                                realEstateProperty.setPromoter(promoter);

                        } else if (ProfilEnum.MOA.equals(promoter.getProfil())) {
                                realEstateProperty.setMoa(promoter);

                        } else if (ProfilEnum.SITE_MANAGER.equals(promoter.getProfil())) {
                                realEstateProperty.setManager(promoter);

                        }

                        // Subscription sub =
                        // subscriptionService.getSubscriptionByUserId(promoter.getId());

                        // realEstateProperty.setSubscription(sub);

                }
                if (request.getMoaId() != 0 &&  request.getMoaId()!=null) {
                        User moa = userRepository.findById(request.getMoaId()).orElse(null);
                        realEstateProperty.setMoa(moa);

                }

                if (request.getManagerId() != 0 && request.getManagerId()  !=null) {
                        User manager = userRepository.findById(request.getManagerId()).orElse(null);
                        realEstateProperty.setMoa(manager);
                }

                if (request.getRecipientId() != 0  && request.getManagerId() !=null ) {
                        User recipient = userRepository.findById(request.getRecipientId()).orElse(null);
                        realEstateProperty.setRecipient(recipient);
                }
                if (request.getNotaryId() != 0) {
                        User notary = userRepository.findById(request.getNotaryId()).orElse(null);
                        realEstateProperty.setNotary(notary);
                }
                if (request.getPropertyTypeId() != 0) {
                        PropertyType propertyType = propertyTypeRepository.findById(request.getPropertyTypeId())
                                        .orElse(null);
                        realEstateProperty.setPropertyType(propertyType);
                }

                if (request.getAgencyId() != 0) {
                        User agency = userRepository.findById(request.getAgencyId()).orElse(null);
                        realEstateProperty.setAgency(agency);

                }

                if (request.getBankId() != 0) {
                        User bank = userRepository.findById(request.getBankId()).orElse(null);
                        realEstateProperty.setBank(bank);

                }

                // Set parent property if applicable
                if (request.getParentPropertyId() != 0) {
                        RealEstateProperty parentProperty = realEstatePropertyRepository
                                        .findById(request.getParentPropertyId())
                                        .orElse(null);
                        realEstateProperty.setParentProperty(parentProperty);
                }

                // Set file names for plan, legal status, and pictures
                realEstateProperty.setPlan(planFileName);
                realEstateProperty.setLegalStatus(legalStatusFileName);
                realEstateProperty.setPictures(pictureUrls);

                // Save the RealEstateProperty entity
                RealEstateProperty savedProperty = realEstatePropertyRepository.save(realEstateProperty);

                return savedProperty;

        }

        @Override
        public RealEstateProperty update(Long id, RealEstatePropertyRequest request, String planFileName,
                        String legalStatusFileName, List<String> pictureUrls) {
                // Retrieve the existing property
                RealEstateProperty existingProperty = realEstatePropertyRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Property not found with id: " + id));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

                // Update fields

                existingProperty.setLotFeesPaid(false);
                existingProperty.setName(request.getName());
                existingProperty.setNumber(request.getNumber());
                existingProperty.setAddress(request.getAddress());
                existingProperty.setPrice(request.getPrice());
                existingProperty.setNumberOfRooms(request.getNumberOfRooms());
                existingProperty.setArea(request.getArea());
                existingProperty.setLatitude(request.getLatitude());
                existingProperty.setLongitude(request.getLongitude());
                existingProperty.setAvailable(request.isAvailable());
                existingProperty.setReservationFee(request.getReservationFee());
                existingProperty.setDescription(request.getDescription());
                existingProperty.setNumberOfLots(request.getNumberOfLots());
                existingProperty.setDiscount(request.getDiscount());
                existingProperty.setLevel(request.getLevel());
                existingProperty.setFeesFile(request.getFeesFile());

                // Mettre à jour les champs booléens pour les équipements communs

                // Update promoter, recipient, and notary

                if (request.getPromoterId() != 0) {
                        User promoter = userRepository.findById(request.getPromoterId())
                                        .orElseThrow(() -> new RuntimeException("Promoter not found"));

                        if (ProfilEnum.PROMOTEUR.equals(promoter.getProfil())) {
                                existingProperty.setPromoter(promoter);

                        } else if (ProfilEnum.MOA.equals(promoter.getProfil())) {
                                existingProperty.setMoa(promoter);

                        } else if (ProfilEnum.SITE_MANAGER.equals(promoter.getProfil())) {
                                existingProperty.setManager(promoter);

                        }

                }
                if (request.getMoaId() != 0) {
                        User moa = userRepository.findById(request.getMoaId()).orElse(null);
                        existingProperty.setMoa(moa);

                }

                if (request.getManagerId() != 0) {
                        User manager = userRepository.findById(request.getManagerId()).orElse(null);
                        existingProperty.setMoa(manager);
                }

                if (request.getRecipientId() != 0) {
                        User recipient = userRepository.findById(request.getRecipientId()).orElse(null);
                        existingProperty.setRecipient(recipient);
                }

                if (request.getNotaryId() != 0) {
                        User notary = userRepository.findById(request.getNotaryId()).orElse(null);
                        existingProperty.setNotary(notary);
                }

                if (request.getAgencyId() != 0) {
                        User agency = userRepository.findById(request.getAgencyId()).orElse(null);
                        existingProperty.setAgency(agency);
                        ;

                }

                if (request.getBankId() != 0) {
                        User bank = userRepository.findById(request.getBankId()).orElse(null);
                        existingProperty.setBank(bank);
                        ;
                }

                // Update parent property if applicable
                if (request.getParentPropertyId() != 0) {
                        RealEstateProperty parentProperty = realEstatePropertyRepository
                                        .findById(request.getParentPropertyId())
                                        .orElse(null);
                        existingProperty.setParentProperty(parentProperty);
                }

                // Update file names for plan, legal status, and pictures
                existingProperty.setPlan(
                                planFileName != null && !planFileName.trim().isEmpty() ? planFileName
                                                : existingProperty.getPlan());
                existingProperty
                                .setLegalStatus(
                                                legalStatusFileName != null && !legalStatusFileName.trim().isEmpty()
                                                                ? legalStatusFileName
                                                                : existingProperty.getLegalStatus());

                if (pictureUrls != null && !pictureUrls.isEmpty()) {
                        List<String> existingPictures = new ArrayList<>(existingProperty.getPictures());
                        existingPictures.addAll(pictureUrls);
                        existingProperty.setPictures(existingPictures);
                }

                return realEstatePropertyRepository.save(existingProperty);

        }

        @Transactional
        public void delete(Long id) {
                RealEstateProperty existingProperty = realEstatePropertyRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Property not found with id: " + id));

                if (existingProperty.getPlan() != null && !existingProperty.getPlan().isEmpty()) {
                        FileTransferUtil.deleteRemoteFile(FileTransferUtil.getRemotePath(existingProperty.getPlan()));
                }

                if (existingProperty.getPictures() != null) {
                        for (String pic : existingProperty.getPictures()) {
                                if (pic != null && !pic.isEmpty()) {
                                        FileTransferUtil.deleteRemoteFile(FileTransferUtil.getRemotePath(pic));
                                }
                        }
                }
                // Now delete the property
                realEstatePropertyRepository.delete(existingProperty);
        }

        @Override
        public Page<RealEstatePropertyResponse> searchByPromoterAndParentIsNull(Long promoterId, String name,
                        Pageable pageable) {

                return realEstatePropertyRepository
                                .searchByPromoterAndName(
                                                promoterId, name, pageable)
                                .map(this::convertToResponse);
        }

        // Conversion method
        private RealEstatePropertyResponse convertToResponse(RealEstateProperty property) {
                RealEstatePropertyResponse response = new RealEstatePropertyResponse();

                // Set basic fields
                response.setId(property.getId());
                response.setName(property.getName());
                response.setNumber(property.getNumber());
                response.setAddress(property.getAddress());

                response.setArea(property.getArea());
                response.setLatitude(property.getLatitude());
                response.setLongitude(property.getLongitude());
                response.setAvailable(property.isAvailable());
                response.setReservationFee(property.getReservationFee());
                response.setDescription(property.getDescription());
                response.setPlan(property.getPlan());
                response.setLegalStatus(property.getLegalStatus());
                response.setNumberOfLots(property.getNumberOfLots());
                response.setLevel(property.getLevel());

                response.setBudget(property.getBudget());
                response.setDiscount(property.getDiscount());
                // Set pictures
                response.setPictures(property.getPictures());

                // Set status
                response.setStatus(property.getStatus());
                response.setPromoter(property.getPromoter());

                response.setPropertyType(property.getPropertyType());
                response.setRecipient(property.getRecipient());

                String qrcode ="S8sxUN1xPUBaKTSvMKpS4e6p6Qa4ZoIZZtXHwLwCn3w=";
                
                // QRCodeUtil.generateQrCode(property.getId().toString(), property.getName());
                response.setQrcode(qrcode);

                if (null != property.getParentProperty()) {
                        response.setParentProperty(property.getParentProperty());
                }

                return response;
        }

        @Override
        public RealEstatePropertyResponse getProperty(Long propertyId) {
                // Fetch the property from the repository (assuming realEstatePropertyRepository
                // is injected)
                RealEstateProperty property = realEstatePropertyRepository.findById(propertyId)
                                .orElseThrow();

                // Convert the entity to a response DTO
                return convertToResponse(property);
        }

}
