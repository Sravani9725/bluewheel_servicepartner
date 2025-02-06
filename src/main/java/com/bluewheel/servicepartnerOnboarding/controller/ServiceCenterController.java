package com.bluewheel.servicepartnerOnboarding.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bluewheel.servicepartnerOnboarding.vo.CommonVO;
import com.bluewheel.servicepartnerOnboarding.vo.FollowUpRepVO;
import com.bluewheel.servicepartnerOnboarding.vo.GetServiceCenterVO;
import com.bluewheel.servicepartnerOnboarding.vo.PhotographyVO;
import com.bluewheel.servicepartnerOnboarding.vo.RetrunResponseVO;
import com.bluewheel.servicepartnerOnboarding.service.ServiceCenterService;
import com.bluewheel.servicepartnerOnboarding.vo.ServiceCenterVO;
import com.bluewheel.servicepartnerOnboarding.vo.VerificationVO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/bluewheel/service")
@Slf4j
@RequiredArgsConstructor
public class ServiceCenterController {
	private final ServiceCenterService serviceCenterService;

	@PostMapping("/service-center")
	public ResponseEntity<RetrunResponseVO> createBusinessRule(@RequestBody @Valid ServiceCenterVO serviceCenter) {
		log.info("Request received to add a service center");
		RetrunResponseVO basevo = serviceCenterService.onBoardServiceCenter(serviceCenter);
//		log.info(basevo);
		return ResponseEntity.ok().body(basevo);

	}
	
	@PostMapping("/service-center/verification")
	public ResponseEntity<RetrunResponseVO> addVerificationDetails(@RequestBody @Valid VerificationVO verificationvo) {
		log.info("Request received to add a service center");
		RetrunResponseVO basevo = serviceCenterService.addVerificationDetails(verificationvo);
//		log.info(basevo);
		return ResponseEntity.ok().body(basevo);

	}
	
	@PostMapping("/service-center/flex")
	public ResponseEntity<RetrunResponseVO> addFlexInstallationDetails(@RequestBody @Valid CommonVO flexvo) {
		log.info("Request received to add a service center");
		RetrunResponseVO basevo = serviceCenterService.addFlexInstallationDetails(flexvo);
//		log.info(basevo);
		return ResponseEntity.ok().body(basevo);

	}
	
	@PostMapping("/service-center/photography")
	public ResponseEntity<RetrunResponseVO> addPhotographyDetails(@RequestBody @Valid CommonVO phVO) {
		log.info("Request received to add a service center");
		RetrunResponseVO basevo = serviceCenterService.addPhotographyDetails(phVO);
//		log.info(basevo);
		return ResponseEntity.ok().body(basevo);

	}
	
	@PostMapping("/service-center/training")
	public ResponseEntity<RetrunResponseVO> addTrainingDetails(@RequestBody @Valid CommonVO phVO) {
		log.info("Request received to add a service center");
		RetrunResponseVO basevo = serviceCenterService.addTrainingDetails(phVO);
//		log.info(basevo);
		return ResponseEntity.ok().body(basevo);

	}
	
	@PostMapping("/service-center/onboard")
	public ResponseEntity<RetrunResponseVO> addOnboardingetails(@RequestBody @Valid CommonVO phVO) {
		log.info("Request received to add a service center");
		RetrunResponseVO basevo = serviceCenterService.addOnboardDetails(phVO);
//		log.info(basevo);
		return ResponseEntity.ok().body(basevo);

	}
	
	@GetMapping("/service-center/{phoneNumber}")
	public ResponseEntity<GetServiceCenterVO> getServiceCenterDetails(@PathVariable  @NotBlank(message="phonenumber cannot be null")String phoneNumber ) {
		log.info("Request received to add a service center");
		GetServiceCenterVO basevo = serviceCenterService.getServiceCenter(phoneNumber);
		log.info("returning service center details");
		return ResponseEntity.ok().body(basevo);

	}
	
	@GetMapping("/service-center/followups/{repId}")
	public ResponseEntity<List<FollowUpRepVO>> getFollowsByRepId(@PathVariable  @NotBlank(message="rep id cannot be null")String repId ) {
		log.info("Request received to add a service center");
		List<FollowUpRepVO> basevo = serviceCenterService.getFollowupsByRepId(repId);
		log.info("returning follow up details");
		return ResponseEntity.ok().body(basevo);

	}
	
	
	
	

}
