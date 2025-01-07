package com.bluewheel.servicepartnerOnboarding.service;





import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.bluewheel.servicepartnerOnboarding.entity.AuthorisedServiceCenterLocation;
import com.bluewheel.servicepartnerOnboarding.entity.AuthorisedServiceCenters;
import com.bluewheel.servicepartnerOnboarding.repository.AuthorisedServiceCenterLocationRepository;
import com.bluewheel.servicepartnerOnboarding.repository.AuthorisedServiceCenterRepository;
import com.bluewheel.servicepartnerOnboarding.vo.AuthorisedServiceCenterVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor

public class AuthorisedServiceCentersService {

    private final AuthorisedServiceCenterRepository authorisedServiceCenterRepository;
    private final AuthorisedServiceCenterLocationRepository authorisedServiceCenterLocationRepository;

    public void createAuthorisedServiceCenters(AuthorisedServiceCenterVO authorisedServiceCenterVO) {
        log.info("Creating authorised service center");

        // Convert VO to Entity
        AuthorisedServiceCenters authorisedServiceCenter = AuthorisedServiceCenters.builder()
                .alternatePhoneNumber(authorisedServiceCenterVO.getAlternatePhoneNumber())
                .businessPhoneNumber(authorisedServiceCenterVO.getBusinessPhoneNumber())
                .category(authorisedServiceCenterVO.getCategory())
                .establishmentYear(authorisedServiceCenterVO.getEstablishmentYear())
                .fuelType(authorisedServiceCenterVO.getFuelType())
                .ownerName(authorisedServiceCenterVO.getOwnerName())
                .serviceCenterName(authorisedServiceCenterVO.getServiceCenterName())
                .transmissionType(authorisedServiceCenterVO.getTransmissionType())
                .email(authorisedServiceCenterVO.getEmail())
                .specialization(authorisedServiceCenterVO.getSpecialization())
                .build();

        // Set timestamps
        Timestamp timestamp = Timestamp.from(Instant.now());
        authorisedServiceCenter.setCreatedAt(timestamp);
        authorisedServiceCenter.setUpdatedAt(timestamp);

        // Save AuthorisedServiceCenters entity
        authorisedServiceCenter = authorisedServiceCenterRepository.save(authorisedServiceCenter);
        log.info("Authorised Service Center created with ID: {}", authorisedServiceCenter.getAuthorisedServiceCenterId());

        // Convert VO to AuthorisedServiceCenterLocation
        AuthorisedServiceCenterLocation authorisedServiceCenterLocation = AuthorisedServiceCenterLocation.builder()
                .authorisedServiceCenters(authorisedServiceCenter)  // setting back reference
                .address(authorisedServiceCenterVO.getAddress())
                .latitude(authorisedServiceCenterVO.getLatitude())
                .longitude(authorisedServiceCenterVO.getLongitude())
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();
        
        // Save AuthorisedServiceCenterLocation entity
        authorisedServiceCenterLocation = authorisedServiceCenterLocationRepository.save(authorisedServiceCenterLocation);
        
        // Update the AuthorisedServiceCenters with the location ID
       // authorisedServiceCenter.setAuthorisedServiceCenterLocationId(authorisedServiceCenterLocation.getAuthorisedServiceCenterLocationId());
       // authorisedServiceCenterRepository.save(authorisedServiceCenter);
       
        log.info("Authorised Service Center Location created for Service Center ID: {}", authorisedServiceCenterLocation.getAuthorisedServiceCenterLocationId());
    }

}

