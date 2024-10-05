package com.bluewheel.servicepartnerOnboarding.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.bluewheel.servicepartnerOnboarding.vo.ErrorDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BluewheelCustomException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	protected String errorMessage;
	protected HttpStatus httpStatus;
	protected String errorCode;
	protected List<ErrorDetails> errors;
	
	public BluewheelCustomException(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public BluewheelCustomException() {
		this.errors =  new ArrayList<>();
	}
	
	

}
