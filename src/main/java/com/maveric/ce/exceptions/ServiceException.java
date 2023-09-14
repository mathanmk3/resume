package com.maveric.ce.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import lombok.Data;

@NoArgsConstructor
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
	public void setHttpStatus(HttpStatus status) {
		this.httpStatus = status;
	}
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public ServiceException(String message) {
		super();
		this.message = message;
	}

	public ServiceException(String message, HttpStatus httpStatus) {
		super();
		this.message = message;
		this.httpStatus = httpStatus;
	}


}
