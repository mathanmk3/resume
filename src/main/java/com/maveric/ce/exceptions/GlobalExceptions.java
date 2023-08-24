package com.maveric.ce.exceptions;

import com.maveric.ce.dto.ErrorDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptions {
	@Autowired
	ErrorCodes ec;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDto> handleBadRequestExceptions(MethodArgumentNotValidException ex) {
		ErrorDto errors = new ErrorDto();

		ex.getBindingResult().getAllErrors().forEach((err) -> {

			String errorMessage = err.getDefaultMessage();
			errors.setErrorCode(err.getCode());
			errors.setErrorMessgae(errorMessage);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

	}
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<ErrorDto> handleServiceException(ServiceException ex) {
		ErrorDto errors = new ErrorDto();
		String messgaeCode = ex.getMessage();
		errors.setErrorMessgae(messgaeCode.split("##")[0]);
		errors.setErrorCode(messgaeCode.split("##")[1]);
		String statsu = HttpStatus.valueOf(Integer.parseInt(errors.getErrorCode())).name();
		return new ResponseEntity<>(errors, HttpStatus.valueOf(statsu));
	}
	
	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<ErrorDto> handleSqlException(DataAccessException ex) {
		ErrorDto errors = new ErrorDto();
		String messgaeCode =ex.getMessage();
		errors.setErrorMessgae(messgaeCode.split("##")[0]);
		errors.setErrorCode(messgaeCode.split("##")[1]);
		String statsu = HttpStatus.valueOf(Integer.parseInt(errors.getErrorCode())).name();
		return new ResponseEntity<>(errors, HttpStatus.valueOf(statsu));
	}

} 
