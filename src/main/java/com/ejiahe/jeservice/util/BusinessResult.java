package com.ejiahe.jeservice.util;

import com.ejiahe.jeservice.util.exception.CodeException;

/**
 * 业务响应结果,可承载简单的错误码和错误信息;
 * 若有其他定制需求,可以继承该类;
 * 
 * 备注: 200表示成功
 * @author MaiLingFeng
 *
 */
public class BusinessResult {

	protected int code = StatusCode.SUCCESS;
	protected String msg;
	
	public BusinessResult(){
		
	}

	public BusinessResult(CodeException e){
		super();
		this.code = e.getCode();
		this.msg = e.getMessage();
	}
	
	public BusinessResult(int code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	
	public boolean isSuccess(){
		return this.code == StatusCode.SUCCESS;
	}
	
}
