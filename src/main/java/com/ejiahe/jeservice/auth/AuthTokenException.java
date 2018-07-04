package com.ejiahe.jeservice.auth;


import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.jeservice.util.exception.CodeException;

import java.util.HashMap;
import java.util.Map;


public class AuthTokenException extends CodeException {
	
	public static final int CODE_ARGUMENT_ILLEGAL = StatusCode.ARGUMENT_ILLEGAL;
	
	public static final int CODE_AUTH_FAILD = StatusCode.AUTH_ERROR;
	
	public static final int CODE_NOT_FOUND = StatusCode.NOT_FOUND;

	public static final int CODE_EXPIRE = StatusCode.ENTITY_NOT_FOUND;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Map<Integer,String> msgTempalte;
	
	static{
		msgTempalte = new HashMap<Integer,String>();
		msgTempalte.put(CODE_AUTH_FAILD, "Token Auth Faild, [{}]");
		msgTempalte.put(CODE_ARGUMENT_ILLEGAL, "Token Encoding Fail, The Argument Illegal");
		msgTempalte.put(CODE_NOT_FOUND, "Token NOT Found: {}");
	}
	

	public AuthTokenException(int code, Object... args) {
		super(code, args);
		// TODO Auto-generated constructor stub
	}


	public AuthTokenException(int code, Throwable cause, Object... args) {
		super(code, cause, args);
		// TODO Auto-generated constructor stub
	}


	public AuthTokenException(int code, Throwable cause) {
		super(code, cause);
		// TODO Auto-generated constructor stub
	}


	public AuthTokenException(int code) {
		super(code);
		// TODO Auto-generated constructor stub
	}


	@Override
	public Map<Integer, String> getMsgTemplate() {
		return msgTempalte;
	}
	
}
