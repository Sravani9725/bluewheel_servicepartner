package com.bluewheel.servicepartnerOnboarding.exception;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.bluewheel.servicepartnerOnboarding.vo.ErrorResponse;

@ControllerAdvice
public class BluewheelCustomExceptionalHandler {
	
	 private ErrorResponse buildErrorDetails(String errorCode, String errorMessage, List<?> errors) {
	        ErrorResponse errorResponse = new ErrorResponse();
	        errorResponse.setMessage(errorMessage);
	        errorResponse.setCode(errorCode);
	        errorResponse.setErrors(errors);
	        return errorResponse;
	    }

	
	@ExceptionHandler(BluewheelBusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BluewheelBusinessException ex, WebRequest request) {
        ErrorResponse errorDetails = buildErrorDetails(ex.getErrorCode(), ex.getErrorMessage(), ex.getErrors());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorDetails);
    }
	
	@ExceptionHandler(BluewheelRuntimeException.class)
    public ResponseEntity<ErrorResponse> BluewheelRuntimeException(BluewheelBusinessException ex, WebRequest request) {
        ErrorResponse errorDetails = buildErrorDetails(ex.getErrorCode(), ex.getErrorMessage(), ex.getErrors());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorDetails);
    }
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleInvalidDateException(HttpMessageNotReadableException ex, WebRequest request) {
		ErrorResponse errorDetails = buildErrorDetails("INVALID_DATA",
				ex.getMessage(), null);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
//		Map<String, String> errors = new HashMap<>();
//		ex.getBindingResult().getAllErrors().forEach((error) -> {
//			String fieldName = ((FieldError) error).getField();
//			String errorMessage = error.getDefaultMessage();
//			errors.put(fieldName, errorMessage);
//		});
//		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		
		// Collect error messages into a list
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .toList();

        // Create ErrorResponse with only messages
        ErrorResponse responseBody = ErrorResponse.builder()
            .code("400")
            .message("Validation failed")
            .errors(errorMessages)
            .build();

        // Return the formatted response
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
	}
	 @ExceptionHandler(DataIntegrityViolationException.class)
	    public ResponseEntity<ErrorResponse> handleDatabaseException(DataIntegrityViolationException ex, WebRequest request) {
	        ErrorResponse errorDetails = buildErrorDetails("DB_FAILURE", ex.getMessage(), null);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
	    }

}
