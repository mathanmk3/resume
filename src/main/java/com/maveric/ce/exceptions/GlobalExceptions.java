package com.maveric.ce.exceptions;

import com.maveric.ce.dto.ErrorDto;

import com.maveric.ce.serviceImpl.CustomerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.ObjectError;
import java.util.List;


@RestControllerAdvice
public class GlobalExceptions   {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptions.class);
	@Autowired
	ErrorCodes ec;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDto> handleBadRequestExceptions(MethodArgumentNotValidException ex) {
		ErrorDto errors = new ErrorDto();
		logger.info("called MethodArgumentNotValidException ");
		logger.info("Exception:"+ex);
		logger.info("Error message: "+ex.getFieldError().getField());
		List <ObjectError>listOfErrors=ex.getBindingResult().getAllErrors();
		String errorslist="";
		for(ObjectError list: listOfErrors){
			System.out.println(list.getObjectName());
			System.out.println(list.getDefaultMessage());
			errorslist=errorslist+","+list.getDefaultMessage();

		}
		logger.info(" list:"+listOfErrors);
		ex.getBindingResult().getAllErrors().forEach((err) -> {
			String errorMessage = err.getDefaultMessage();
			errors.setErrorCode(err.getCode());
			logger.info("individual error:"+ex.getFieldError().getField());
			//errors.setErrorMessgae(ex.getFieldError().getField()+" "+errorMessage);
			//errors.setErrorMessgae(errorslist);
		});
		errors.setErrorMessgae(errorslist);
		logger.error("final error:"+errors.getErrorMessgae());
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<ErrorDto> handleServiceException(ServiceException ex) {
		ErrorDto errors = new ErrorDto();
		String messgaeCode = ex.getMessage();
		if (messgaeCode.contains("#")) {
			errors.setErrorMessgae(messgaeCode.split("##")[0]);
			errors.setErrorCode(messgaeCode.split("##")[1]);
			String statsu = HttpStatus.valueOf(Integer.parseInt(errors.getErrorCode())).name();
			ex.getStackTrace();
			return new ResponseEntity<>(errors, HttpStatus.valueOf(statsu));
		} else {
			errors.setErrorMessgae(messgaeCode);
			errors.setErrorCode("500");
			return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@ExceptionHandler(SQLExceptions.class)
	public ResponseEntity<ErrorDto> handleSqlException(SQLExceptions ex) {
		ErrorDto errors = new ErrorDto();
		String messgaeCode = ex.getMessage();
		if (messgaeCode.contains("#")) {
			errors.setErrorMessgae(messgaeCode.split("##")[0]);
			errors.setErrorCode(messgaeCode.split("##")[1]);
			String statsu = HttpStatus.valueOf(Integer.parseInt(errors.getErrorCode())).name();
			ex.getStackTrace();
			return new ResponseEntity<>(errors, HttpStatus.valueOf(statsu));
		} else {
			errors.setErrorMessgae("INTERNAL_SERVER_ERROR");
			errors.setErrorCode("500");
			return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorDto> handleDefaultException(HttpMessageNotReadableException ex) {
		ErrorDto errors = new ErrorDto();
		if (ex.getMessage().contains("CurrencyType")){
			errors.setErrorMessgae("INVALID_CurrencyType");
		}else if(ex.getMessage().contains("Role")){
			errors.setErrorMessgae("INVALID_Role");
		}else if(ex.getMessage().contains("Gender")){
			errors.setErrorMessgae("INVALID_GENDER");
		}
		errors.setErrorCode("500");
		return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
