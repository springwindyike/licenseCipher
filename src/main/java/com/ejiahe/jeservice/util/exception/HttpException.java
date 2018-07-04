package com.ejiahe.jeservice.util.exception;


public class HttpException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HttpException(String message, Throwable cause) {
		super(message, cause);
	}

	public HttpException(String message) {
		super(message);
	}
	
	
	
}
