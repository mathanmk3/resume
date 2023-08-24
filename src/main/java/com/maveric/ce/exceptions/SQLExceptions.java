package com.maveric.ce.exceptions;

import org.springframework.dao.DataAccessException;

public class SQLExceptions extends DataAccessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SQLExceptions(String msg) {
		super(msg);
	}

}
