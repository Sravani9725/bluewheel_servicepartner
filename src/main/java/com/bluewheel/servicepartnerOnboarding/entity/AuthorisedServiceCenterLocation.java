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
@Table(name = "authorised_service_center_location", schema="servicepartner_onboarding_sales")
@NamedQuery(name = "AuthorisedServiceCenterLocation.findAll", query = "SELECT a FROM AuthorisedServiceCenterLocation a")
public class AuthorisedServiceCenterLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="authorised_service_center_location_id")
    private Integer authorisedServiceCenterLocationId;
    
    private String address;

    @Column(name = "created_at")
    private Timestamp createdAt;

    private Double latitude;

    private Double longitude;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // bi-directional one-to-one association to AuthorisedServiceCenters
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authorised_service_center_id")
    private AuthorisedServiceCenters authorisedServiceCenters;
}
