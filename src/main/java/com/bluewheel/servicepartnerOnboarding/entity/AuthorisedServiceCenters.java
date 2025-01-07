package com.bluewheel.servicepartnerOnboarding.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "authorised_service_centers", schema="servicepartner_onboarding_sales")
@NamedQuery(name = "AuthorisedServiceCenters.findAll", query = "SELECT a FROM AuthorisedServiceCenters a")
public class AuthorisedServiceCenters {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="authorised_service_center_id")
    private Integer authorisedServiceCenterId;

    @Column(name = "business_phone_number")
    private String businessPhoneNumber;

    @Column(name = "alternate_phone_number")
    private String alternatePhoneNumber;
    
    @Column(name = "owner_name")
    private String ownerName;

    private Integer establishmentYear;
    
    private String specialization;

    @Column(name = "fuel_type")
    private String fuelType;

    private String category;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name ="transmission_type")
    private String transmissionType;
    
    private String email;
    
    @Column(name ="service_center_name")
    private String serviceCenterName;
    
    @OneToOne(fetch = FetchType.EAGER,  mappedBy = "authorisedServiceCenters")
    private AuthorisedServiceCenterLocation authorisedServiceCenterLocation;
}

