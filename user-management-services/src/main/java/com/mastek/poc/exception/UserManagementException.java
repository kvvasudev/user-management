package com.mastek.poc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserManagementException extends RuntimeException {

	private static final long serialVersionUID = -1652935961083739303L;

	public UserManagementException() {
		super();
	}

	public UserManagementException(String message) {
		super(message);
	}
	
}
