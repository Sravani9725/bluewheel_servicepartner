package com.bluewheel.servicepartnerOnboarding.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BluewheelBusinessException extends BluewheelCustomException{
	
	public BluewheelBusinessException(String errorMessage, HttpStatus status, String errorCode) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.httpStatus = status;
		this.errorCode = errorCode;
	}

}
