package com.abdelrahman.appointmentscheduling.exception;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandling {

	@ExceptionHandler(TimeNotValidException.class)
	public ResponseEntity<?> handleTimeNotValid(TimeNotValidException ex,HttpServletRequest http) {
		String path = http.getRequestURI();
		ErrorResponse error = new ErrorResponse(
				ex.getMessage(),
				Arrays.asList(ex.getLocalizedMessage()),
				HttpStatus.NOT_ACCEPTABLE.value(),
				path
				);
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE.value()).body(error);
	}
	
	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<?> handleRecordNotFound(RecordNotFoundException ex,HttpServletRequest http) {
		String path = http.getRequestURI();
		ErrorResponse error = new ErrorResponse(
				ex.getMessage(),
				Arrays.asList(ex.getLocalizedMessage()),
				HttpStatus.NOT_FOUND.value(),
				path
				);
		return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> handleRuntime(RuntimeException ex,HttpServletRequest http) {
		String path = http.getRequestURI();
		ErrorResponse error = new ErrorResponse(
				ex.getMessage(),
				Arrays.asList(ex.getLocalizedMessage()),
				HttpStatus.BAD_REQUEST.value(),
				path
				);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(error);
	}
}
