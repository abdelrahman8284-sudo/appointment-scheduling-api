package com.abdelrahman.appointmentscheduling.exception;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class ErrorResponse {

	private final boolean success = false;
	
	private String message;
	
	private List<String> details;
	
	private LocalDateTime timestamp;
	
	private String path ;
	
	private int status;
	
	public ErrorResponse() {
		this.timestamp = LocalDateTime.now();
	}
	

	public ErrorResponse(String message, List<String> details, int status,String path) {
		super();
		this.message = message;
		this.details = details;
		this.timestamp = LocalDateTime.now();
		this.status = status;
		this.path = path;
	}
}
