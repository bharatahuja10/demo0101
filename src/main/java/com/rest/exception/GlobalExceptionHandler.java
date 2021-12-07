package com.rest.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	// Handle Specific Exceptions
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {

		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IncompleteEmployeeDataException.class)
	public ResponseEntity<?> handleResourceNotFoundException(IncompleteEmployeeDataException exception,
			WebRequest request) {

		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
				"Incomplete Data In Request Body");
		return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
	}

	// Handle Global Exceptions
	// Parent Exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGlobalException(Exception exception, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));

		return new ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
