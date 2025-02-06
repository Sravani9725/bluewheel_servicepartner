package com.bluewheel.servicepartnerOnboarding.service;

import java.util.List;

import com.bluewheel.servicepartnerOnboarding.vo.CommonVO;
import com.bluewheel.servicepartnerOnboarding.vo.FollowUpRepVO;
import com.bluewheel.servicepartnerOnboarding.vo.GetServiceCenterVO;
import com.bluewheel.servicepartnerOnboarding.vo.PhotographyVO;
import com.bluewheel.servicepartnerOnboarding.vo.RetrunResponseVO;
import com.bluewheel.servicepartnerOnboarding.vo.ServiceCenterVO;
import com.bluewheel.servicepartnerOnboarding.vo.VerificationVO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public interface ServiceCenterService {

	RetrunResponseVO onBoardServiceCenter(@Valid ServiceCenterVO serviceCenter);

	RetrunResponseVO addVerificationDetails(@Valid VerificationVO verificationvo);

	RetrunResponseVO addFlexInstallationDetails(@Valid CommonVO flexvo);

	RetrunResponseVO addPhotographyDetails( @Valid CommonVO phVO);

	RetrunResponseVO addTrainingDetails(@Valid CommonVO phVO);

	RetrunResponseVO addOnboardDetails(@Valid CommonVO phVO);

	GetServiceCenterVO getServiceCenter(@NotBlank(message = "phonenumber cannot be null") String phoneNumber);

	List<FollowUpRepVO> getFollowupsByRepId(@NotBlank(message = "rep id cannot be null") String repId);

}
