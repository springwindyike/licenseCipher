package com.ejiahe.sdk.openbean;

import com.ejiahe.jeservice.util.CodeResult;

public class UserLoginInfo extends CodeResult {

	private String user_id;
	private String name;
	private String client_type;
	private String corp_id;
	private String member_role;
	private String agent_id;
	private String app_name;

	//用户鉴权code
	private String code;

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClient_type() {
		return client_type;
	}
	public void setClient_type(String client_type) {
		this.client_type = client_type;
	}
	public String getCorp_id() {
		return corp_id;
	}
	public void setCorp_id(String corp_id) {
		this.corp_id = corp_id;
	}
	public String getMember_role() {
		return member_role;
	}
	public void setMember_role(String member_role) {
		this.member_role = member_role;
	}
	public String getAgent_id() {
		return agent_id;
	}
	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "UserLoginInfo [user_id=" + user_id + ", name=" + name
				+ ", client_type=" + client_type + ", corp_id=" + corp_id
				+ ", member_role=" + member_role + ", agent_id=" + agent_id
				+ "]";
	}
	
	
}
