package com.ejiahe.jeservice.util.common;

import com.ejiahe.jeservice.util.CodeResult;
import com.ejiahe.jeservice.util.exception.CodeException;

import java.util.Collections;
import java.util.Map;

public class GZBApiException extends CodeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public GZBApiException(CodeResult result){
		super(result.getResp_code(),result.getResp_msg());
	}
	
	public GZBApiException(int code, Object... args) {
		super(code, args);
		// TODO Auto-generated constructor stub
	}

	public GZBApiException(int code, String msg, Throwable cause) {
		super(code, msg, cause);
		// TODO Auto-generated constructor stub
	}

	public GZBApiException(int code, String msg) {
		super(code, msg);
		// TODO Auto-generated constructor stub
	}

	public GZBApiException(int code, Throwable cause, Object... args) {
		super(code, cause, args);
		// TODO Auto-generated constructor stub
	}

	public GZBApiException(int code, Throwable cause) {
		super(code, cause);
		// TODO Auto-generated constructor stub
	}

	public GZBApiException(int code) {
		super(code);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<Integer, String> getMsgTemplate() {
		// TODO Auto-generated method stub
		return Collections.emptyMap();
	}

}
