package com.ejiahe.app.license.uitls.exception;


import com.ejiahe.app.license.uitls.StrUtils;

import java.util.Map;

/**
 * 带错误码的异常;
 * 
 * 
 * @author MaiJingFeng
 */
public abstract class CodeRuntimeException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected int code;
	
	protected Object[] args;
	
	public CodeRuntimeException(int code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public CodeRuntimeException(int code){
		super();
		this.code = code;
	}
	
	public CodeRuntimeException(int code, String msg){
		super(msg);
		this.code = code;
	}
	
	public CodeRuntimeException(int code, String msg, Throwable cause){
		super(msg, cause);
		this.code = code;
	}
	
	public CodeRuntimeException(int code, Object ...args){
		this(code);
		this.args = args;
	}
	
	public CodeRuntimeException(int code,Throwable cause, Object ...args){
		this(code,cause);
		this.args = args;
	}
	
	
	public abstract Map<Integer,String> getMsgTemplate();

	
	protected String getMsgTemplate(int code){
		Map<Integer,String> templates = getMsgTemplate();
		if(templates != null){
			return templates.get(code);
		}
		
		return null;
	}
	
	@Override
	public String getMessage() {
		String msg = getMsgTemplate(this.code); 
		if(msg == null){
			msg = super.getMessage();
		}
				
		if(msg != null && args != null){
			return StrUtils.arrayFormat(msg, args);
		}else{
			return msg;
		}
	}
	
	
	public int getCode(){
		return this.code;
	}
	
}
