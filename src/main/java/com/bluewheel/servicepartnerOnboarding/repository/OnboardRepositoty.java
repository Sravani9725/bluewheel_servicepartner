package com.bluewheel.servicepartnerOnboarding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluewheel.servicepartnerOnboarding.entity.Flex;
import com.bluewheel.servicepartnerOnboarding.entity.Onboard;
import com.bluewheel.servicepartnerOnboarding.entity.Photography;
import com.bluewheel.servicepartnerOnboarding.entity.ServiceCenter;

import jakarta.validation.constraints.NotBlank;

public interface OnboardRepositoty extends JpaRepository<Onboard, Integer> {

	Onboard getByServiceCenter(ServiceCenter center);

	List<Onboard> getByTrRepId(@NotBlank(message = "rep id cannot be null") String repId);

}
