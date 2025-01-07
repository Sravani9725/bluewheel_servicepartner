package com.bluewheel.servicepartnerOnboarding.vo;



import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class AuthorisedServiceCenterVO  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer authorisedServiceCenterId;
	
	@Pattern(regexp = "^$|^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number provided.")
	private String alternatePhoneNumber;
	
	
	@Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number provided.")
	private String businessPhoneNumber;
	
	private String category;

	private Timestamp createdAt;

	@Pattern(regexp ="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}", message="Please provide valid email address")
//	@NotBlank(message = "Email field cannot be empty.")
//	@Size(min = ApplicationConstants.EMAIL_MIN_SIZE, max = ApplicationConstants.EMAIL_MAX_SIZE)
	private String email;
	
	private Integer establishmentYear;
	
	private String serviceCenterName;

	private String ownerName;
	
	private String specialization;

	private Timestamp updatedAt;
	
	private String fuelType;
	
	private String transmissionType;
	
	private String address;
	
	private Double longitude;
	
	private Double latitude;
	
	
	
	
	

}

