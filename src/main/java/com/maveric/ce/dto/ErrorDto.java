package com.maveric.ce.dto;

import lombok.Data;

@Data
public class ErrorDto {

	private int httpStatus;
	private String errorMessgae;
	private String errorCode;

}
