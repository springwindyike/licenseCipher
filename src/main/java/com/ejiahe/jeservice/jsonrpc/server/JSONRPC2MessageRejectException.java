package com.ejiahe.jeservice.jsonrpc.server;

public class JSONRPC2MessageRejectException extends IllegalAccessException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JSONRPC2Interceptor interceptor;
	
	public JSONRPC2MessageRejectException(JSONRPC2Interceptor interceptor , String message){
		super(message);
		this.interceptor = interceptor;
	}
	
	
	public JSONRPC2Interceptor getInterceptor(){
		return interceptor;
	}
	
}
