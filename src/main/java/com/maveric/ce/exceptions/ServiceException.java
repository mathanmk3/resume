package com.maveric.ce.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Data;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String code;
	private String message;
	private StackTraceElement[] stackTrace;
	private String exceptionCaughtMessage;
	private HttpStatus httpStatus;
	




	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = stackTrace;
	}

	
	public ServiceException(final Exception exception) {
		this.message = exception.getMessage();
		this.stackTrace = exception.getStackTrace();
	}


	public ServiceException(String message) {
		super();
		this.message = message;
	}

	public ServiceException(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
	

	public ServiceException(String message, HttpStatus httpStatus) {
		super();
		this.message = message;
		this.httpStatus = httpStatus;
	}

	public ServiceException(String code, String message, String exceptionCaughtMessage) {
		super();
		this.code = code;
		this.message = message;
		this.exceptionCaughtMessage = exceptionCaughtMessage;
	}

	

}
