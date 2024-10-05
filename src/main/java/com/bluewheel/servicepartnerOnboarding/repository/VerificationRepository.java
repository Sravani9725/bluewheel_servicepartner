package com.bluewheel.servicepartnerOnboarding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluewheel.servicepartnerOnboarding.entity.ServiceCenter;
import com.bluewheel.servicepartnerOnboarding.entity.Verification;

import jakarta.validation.constraints.NotBlank;

public interface VerificationRepository extends JpaRepository<Verification, Integer> {

	Verification findByServiceCenter(ServiceCenter center);

	List<Verification> getByVerifierRepId(@NotBlank(message = "rep id cannot be null") String repId);

}
