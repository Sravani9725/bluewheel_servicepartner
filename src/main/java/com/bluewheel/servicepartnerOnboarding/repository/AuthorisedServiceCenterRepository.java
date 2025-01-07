package com.bluewheel.servicepartnerOnboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bluewheel.servicepartnerOnboarding.entity.AuthorisedServiceCenters;



@Repository
public interface AuthorisedServiceCenterRepository extends  JpaRepository<AuthorisedServiceCenters, Integer>{

}
