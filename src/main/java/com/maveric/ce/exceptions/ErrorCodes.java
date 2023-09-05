package com.maveric.ce.exceptions;

import org.springframework.stereotype.Component;

@Component
public final class ErrorCodes {

	public static final String API_EMPTY_RESPONSE = "No respnse from API##404";
	public static final String NO_ORDER_FOUND = "No order found##403";
	public static final String INVALID_CURRENCY_API = "Bad API Call##500";
	public static final String BALANCE_NOT_FOUND = "zero balance found##404";
	public static final String ERROR_IN_UPDATING_BALANCE = "Error while updating balance##500";
	public static final String INSUFFICIENT_BALANCE = "Insufficient balance ##400";
	public static final String	CONNECTION_ISSUE ="Server Connection Isusue ##500";

	//
	public static final String ACCOUNT_NOT_FOUND = "No account found##404";
	public static final String INVALID_AUTHENTICATION ="Invalid username/Password##403";
	public  static final String ACCOUNT_ALREADY_EXISTS = "Account Already Exists##409";
	public static final String CUSTOMER_NOT_FOUND = "Customer Not found##404";
	public static final String UN_AUTHORIZED = "Unauthorized access##401";
	public static final String INVALID_TOKEN="Token is invalid##403";
	public static final String TOKEN_EXPIRED="Token is Expired##403";
	public static final String Server_Expection ="Server failed to start##500";

	public static final String PAGE_NOT_FOUND ="Page not found";

	public static final String EMAIL_ALREADY_EXISTS ="Email already exists##404";

	public static final String SAME_CURRENCY_FOUND ="Cannot Convert Same Currency Type##406";

}
