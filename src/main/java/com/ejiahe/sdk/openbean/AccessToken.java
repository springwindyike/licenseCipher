package com.ejiahe.sdk.openbean;

import com.ejiahe.jeservice.util.CodeResult;
import com.ejiahe.jeservice.util.JEConstants;
import com.google.common.base.Strings;

import java.util.Date;

public class AccessToken extends CodeResult {

	
	private String access_token;
	private int expire_in;
	private Date creationDate;
	
	public AccessToken(){
		super();
		this.creationDate = new Date();
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String accessToken) {
		access_token = accessToken;
	}

	public int getExpire_in() {
		return expire_in;
	}

	public void setExpire_in(int expiresIn) {
		expire_in = expiresIn;
	}
	
	
	public boolean isExpire(){
		if(!Strings.isNullOrEmpty(access_token)){
			int lifetime = (int)((System.currentTimeMillis() - creationDate.getTime()) / JEConstants.SECOND);
			if(lifetime + 10 * 60 > expire_in){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}

}
