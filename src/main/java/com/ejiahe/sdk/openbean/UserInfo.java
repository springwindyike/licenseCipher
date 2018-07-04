package com.ejiahe.sdk.openbean;

import com.ejiahe.jeservice.util.CodeResult;

import java.util.ArrayList;
import java.util.List;


public class UserInfo extends CodeResult {

	private String user_id;
	private String member_alias;
	private String status;
	private String custom_id;
	private String sip_account;
	private String mobile;
	private String email;
	private String name;
	private String avartar;
	private String gender;
	private String birthday;
	private String signature;
	private String position;//职位
	private List<String> departments;
	
	
	public List<String> getDepartments() {
		return departments == null ? new ArrayList<String>() : departments;
	}
	public void setDepartments(List<String> departments) {
		this.departments = departments;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getMember_alias() {
		return member_alias;
	}
	public void setMember_alias(String member_alias) {
		this.member_alias = member_alias;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCustom_id() {
		return custom_id;
	}
	public void setCustom_id(String custom_id) {
		this.custom_id = custom_id;
	}
	public String getSip_account() {
		return sip_account;
	}
	public void setSip_account(String sip_account) {
		this.sip_account = sip_account;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvartar() {
		return avartar;
	}
	public void setAvartar(String avartar) {
		this.avartar = avartar;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	@Override
	public String toString() {
		return "UserInfo [user_id=" + user_id + ", member_alias="
				+ member_alias + ", status=" + status + ", custom_id="
				+ custom_id + ", sip_account=" + sip_account + ", mobile="
				+ mobile + ", email=" + email + ", name=" + name + ", avatar="
				+ avartar + ", gender=" + gender + ", birthday=" + birthday
				+ ", signature=" + signature + "]";
	}


}
