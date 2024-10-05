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
public class BluewheelRuntimeException extends BluewheelCustomException{
	
	public BluewheelRuntimeException(String errorMessage, HttpStatus status, String errorCode) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.httpStatus = status;
		this.errorCode = errorCode;
	}
	
	public BluewheelRuntimeException(String errorMessage, HttpStatus httpStatus, String errorCode, List<ErrorDetails> list) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		if (this.errors == null) {
			this.errors = new ArrayList<>();
		}
		this.errors.addAll(list);
	}

	

}
