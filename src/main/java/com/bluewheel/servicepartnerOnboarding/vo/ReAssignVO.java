package com.bluewheel.servicepartnerOnboarding.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter 
public class ReAssignVO {
	@NotBlank(message="Phone number is mandatory")
	private String phoneNumber;
	@NotBlank(message="Category type is mandatory")
	private String categoryType;
	@NotBlank(message="Rep id is mandatory")
	private String repId;
}
