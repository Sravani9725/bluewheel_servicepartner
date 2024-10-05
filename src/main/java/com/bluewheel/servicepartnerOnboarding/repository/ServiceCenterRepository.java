package com.bluewheel.servicepartnerOnboarding.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluewheel.servicepartnerOnboarding.entity.ServiceCenter;

import jakarta.validation.constraints.NotBlank;

public interface ServiceCenterRepository extends JpaRepository<ServiceCenter, Integer>{

	ServiceCenter findByServiceCenterPhoneNumber(String phoneNumber);

	List<ServiceCenter> findBySalesRepId(@NotBlank(message = "rep id cannot be null") String repId);

}
