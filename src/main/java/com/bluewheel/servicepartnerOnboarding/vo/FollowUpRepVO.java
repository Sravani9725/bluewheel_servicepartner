package com.bluewheel.servicepartnerOnboarding.vo;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FollowUpRepVO {
	
	private String serviceCenterName;
	private String serviceCenterOwnerName;
	private String serviceCenterAddress;
	private String serviceCenterPhonenumber;
	private FollowUpVO followUpDetails;

}
