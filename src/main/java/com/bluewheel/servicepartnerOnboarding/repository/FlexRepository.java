package com.bluewheel.servicepartnerOnboarding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluewheel.servicepartnerOnboarding.entity.Flex;
import com.bluewheel.servicepartnerOnboarding.entity.ServiceCenter;

import jakarta.validation.constraints.NotBlank;

public interface FlexRepository extends JpaRepository<Flex, Integer> {

	Flex getByServiceCenter(ServiceCenter center);

	List<Flex> getByFlexRepId(@NotBlank(message = "rep id cannot be null") String repId);

}
