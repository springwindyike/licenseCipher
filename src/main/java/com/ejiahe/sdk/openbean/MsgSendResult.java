package com.ejiahe.sdk.openbean;

import com.ejiahe.jeservice.util.CodeResult;

import java.util.List;

public class MsgSendResult extends CodeResult {

	private String message_id;
	
	private List<String> invalid_users;
	
	private List<String> invalid_member_aliases;
	
	private List<String> invalid_departments;
	
	private List<String> invalid_chatrooms;

	

	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	public List<String> getInvalid_users() {
		return invalid_users;
	}
	public void setInvalid_users(List<String> invalid_users) {
		this.invalid_users = invalid_users;
	}



	public List<String> getInvalid_member_aliases() {
		return invalid_member_aliases;
	}



	public void setInvalid_member_aliases(List<String> invalid_member_aliases) {
		this.invalid_member_aliases = invalid_member_aliases;
	}



	public List<String> getInvalid_departments() {
		return invalid_departments;
	}



	public void setInvalid_departments(List<String> invalid_departments) {
		this.invalid_departments = invalid_departments;
	}



	public List<String> getInvalid_chatrooms() {
		return invalid_chatrooms;
	}



	public void setInvalid_chatrooms(List<String> invalid_chatrooms) {
		this.invalid_chatrooms = invalid_chatrooms;
	}



	@Override
	public String toString() {
		return "MsgSendResult [message_id=" + message_id + ", invalid_users="
				+ invalid_users + ", invalid_member_aliases="
				+ invalid_member_aliases + ", invalid_departments="
				+ invalid_departments + ", invalid_chatrooms="
				+ invalid_chatrooms + "]";
	}
	
	
}
