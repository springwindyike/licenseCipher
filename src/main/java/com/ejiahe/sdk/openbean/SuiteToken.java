package com.ejiahe.sdk.openbean;

import com.ejiahe.jeservice.util.CodeResult;
import com.ejiahe.jeservice.util.JEConstants;
import com.google.common.base.Strings;

import java.util.Date;

public class SuiteToken extends CodeResult {

	
	private String suite_access_token;
	private int expire_in;
	private Date creationDate;
	
	public SuiteToken(){
		super();
		this.creationDate = new Date();
	}
	

	public String getSuite_access_token() {
		return suite_access_token;
	}



	public void setSuite_access_token(String suite_access_token) {
		this.suite_access_token = suite_access_token;
	}



	public int getExpire_in() {
		return expire_in;
	}

	public void setExpire_in(int expiresIn) {
		expire_in = expiresIn;
	}
	
	
	public boolean isExpire(){
		if(Strings.isNullOrEmpty(suite_access_token) || 
				(expire_in * JEConstants.SECOND - 10 * JEConstants.MINUTE)
				+ creationDate.getTime() < System.currentTimeMillis()
				){
			return true;
		}else{
			return false;
		}
	}

}
