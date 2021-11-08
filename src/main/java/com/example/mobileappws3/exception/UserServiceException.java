package com.example.mobileappws3.exception;

public class UserServiceException extends RuntimeException{

	
	private static final long serialVersionUID = 7353683880740185992L;

	
	// constructor
	public UserServiceException(String message) { 
		super(message);
	}

}
