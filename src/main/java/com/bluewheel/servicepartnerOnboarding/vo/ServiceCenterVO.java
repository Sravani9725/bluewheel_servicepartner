package com.bluewheel.servicepartnerOnboarding.vo;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServiceCenterVO {
	
	@NotBlank(message="Please provide phoneNumber")
	private String phoneNumber;
	
	private String serviceCenterOwnerName;
	
	private String serviceCenterAddress;
	
	private String serviceCenterName;
	
	@NotBlank(message="sales person rep id is mandatory")
	private String salesRepId;
	
	@FutureOrPresent(message="Registered date cannot be of a past time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate registeredDate;
	
	private String comments;
	
	private String registrationStatus;
	
	private String subscriptionType;
	
	private FollowUpVO followup;

}
