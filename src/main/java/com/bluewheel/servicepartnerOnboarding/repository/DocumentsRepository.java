package com.bluewheel.servicepartnerOnboarding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluewheel.servicepartnerOnboarding.entity.Documents;
import com.bluewheel.servicepartnerOnboarding.entity.Flex;
import com.bluewheel.servicepartnerOnboarding.entity.Onboard;
import com.bluewheel.servicepartnerOnboarding.entity.ServiceCenter;
import com.bluewheel.servicepartnerOnboarding.entity.Training;
import com.bluewheel.servicepartnerOnboarding.entity.Verification;

public interface DocumentsRepository extends JpaRepository<Documents, Integer> {

	List<Documents> findByServiceCenter(ServiceCenter serviceCenter);

	List<Documents> findByFlex(Flex flex);

	List<Documents> findByOnboard(Onboard onboard);

	List<Documents> findByVerification(Verification verification);

	List<Documents> findByTraining(Training training);

	List<Documents> getByServiceCenterAndDocCategory(ServiceCenter serviceCenter, String docCategory);

	List<Documents> getByFlexAndDocCategory(Flex flex, String docCategory);

	List<Documents> getByOnboardAndDocCategory(Onboard serviceCenter, String docCategory);

	List<Documents> getByVerificationAndDocCategory(Verification serviceCenter, String docCategory);

	List<Documents> getByTrainingAndDocCategory(Training training, String docCategory);

}
