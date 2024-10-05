package com.bluewheel.servicepartnerOnboarding.vo;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VerificationVO {
	
	
	private String verifierName;
	@NotBlank(message="sales person rep id is mandatory")
	private String verifierRepId;
	
	private String verificationStatus;
	@FutureOrPresent(message="Flex Installation date cannot be of a past time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate flexInstallationDate;
	
	private String flexDimensions;
	
	private String comments;
	@NotBlank(message="Please provide phoneNumber")
	private String phoneNumber;
	
	private FollowUpVO followup;

}
