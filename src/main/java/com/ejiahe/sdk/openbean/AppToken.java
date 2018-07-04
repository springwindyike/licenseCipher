package com.ejiahe.sdk.openbean;

import com.ejiahe.jeservice.util.CodeResult;
import com.ejiahe.jeservice.util.JEConstants;
import com.google.common.base.Strings;

import java.util.Date;

public class AppToken extends CodeResult {


	private String app_access_token;
	private int expire_in;
	private Date creationDate;

	public AppToken(){
		super();
		this.creationDate = new Date();
	}
	

	public String getApp_access_token() {
		return app_access_token;
	}



	public void setApp_access_token(String app_access_token) {
		this.app_access_token = app_access_token;
	}



	public int getExpire_in() {
		return expire_in;
	}

	public void setExpire_in(int expiresIn) {
		expire_in = expiresIn;
	}
	
	
	public boolean isExpire(){
		if(Strings.isNullOrEmpty(app_access_token) ||
				(expire_in * JEConstants.SECOND - 10 * JEConstants.MINUTE)
				+ creationDate.getTime() < System.currentTimeMillis()
				){
			return true;
		}else{
			return false;
		}
	}

}
