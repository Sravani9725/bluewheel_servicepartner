package com.bluewheel.servicepartnerOnboarding.service;

import java.util.List;

import com.bluewheel.servicepartnerOnboarding.vo.CommonVO;
import com.bluewheel.servicepartnerOnboarding.vo.FollowUpRepVO;
import com.bluewheel.servicepartnerOnboarding.vo.GetServiceCenterVO;
import com.bluewheel.servicepartnerOnboarding.vo.PhotographyVO;
import com.bluewheel.servicepartnerOnboarding.vo.ServiceCenterVO;
import com.bluewheel.servicepartnerOnboarding.vo.VerificationVO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public interface ServiceCenterService {

	String onBoardServiceCenter(@Valid ServiceCenterVO serviceCenter);

	String addVerificationDetails(@Valid VerificationVO verificationvo);

	String addFlexInstallationDetails(@Valid CommonVO flexvo);

	String addPhotographyDetails( @Valid CommonVO phVO);

	String addTrainingDetails(@Valid CommonVO phVO);

	String addOnboardDetails(@Valid CommonVO phVO);

	GetServiceCenterVO getServiceCenter(@NotBlank(message = "phonenumber cannot be null") String phoneNumber);

	List<FollowUpRepVO> getFollowupsByRepId(@NotBlank(message = "rep id cannot be null") String repId);

}
