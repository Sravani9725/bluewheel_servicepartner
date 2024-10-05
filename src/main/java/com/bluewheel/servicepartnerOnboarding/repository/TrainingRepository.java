package com.bluewheel.servicepartnerOnboarding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluewheel.servicepartnerOnboarding.entity.Flex;
import com.bluewheel.servicepartnerOnboarding.entity.Photography;
import com.bluewheel.servicepartnerOnboarding.entity.ServiceCenter;
import com.bluewheel.servicepartnerOnboarding.entity.Training;

import jakarta.validation.constraints.NotBlank;

public interface TrainingRepository extends JpaRepository<Training, Integer> {

	Training getByServiceCenter(ServiceCenter center);

	List<Training> getByTrRepId(@NotBlank(message = "rep id cannot be null") String repId);

}
