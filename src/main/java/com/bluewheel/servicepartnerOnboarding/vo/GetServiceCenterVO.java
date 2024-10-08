package com.bluewheel.servicepartnerOnboarding.vo;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GetServiceCenterVO {
	
	
	private String phoneNumber;
	
	private String serviceCenterOwnerName;
	
	private String serviceCenterAddress;
	
	private String serviceCenterName;
	
	private String salesRepId;
	
	private Double latitude;
	
	private Double longitude;
	
	private LocalDate registeredDate;
	
	private String registrationComments;
	
	private String registrationStatus;
	
	private String subscriptionType;
	
	private FollowUpVO registrationFollowup;
	
	private VerificationVO verificationDetails;
	
	private CommonVO flexDetails;
	private CommonVO photographyDetails;
	private CommonVO trainingDetails;
	private CommonVO onBoardingDetails;
	

	
	
	
	
}
