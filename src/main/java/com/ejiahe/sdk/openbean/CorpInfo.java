package com.ejiahe.sdk.openbean;

import com.ejiahe.jeservice.util.CodeResult;

public class CorpInfo extends CodeResult {

	private String corp_id;
	private String name;
	private String logo;
	public String getCorp_id() {
		return corp_id;
	}
	public void setCorp_id(String corp_id) {
		this.corp_id = corp_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
}
