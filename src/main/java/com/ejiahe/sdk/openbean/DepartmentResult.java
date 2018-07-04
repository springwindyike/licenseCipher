package com.ejiahe.sdk.openbean;

import com.ejiahe.jeservice.util.CodeResult;

import java.util.List;

/**
 * 部门
 * @author focus
 * @date 2016年3月2日
 * @time 下午1:40:40
 */
public class DepartmentResult extends CodeResult {

	private String department_id;
	private String name;
	private String department_alias;
	private String parent_id;
	private Integer order;
	private List<UserInfo> users;
	
	public String getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartment_alias() {
		return department_alias;
	}
	public void setDepartment_alias(String department_alias) {
		this.department_alias = department_alias;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public List<UserInfo> getUsers() {
		return users;
	}
	public void setUsers(List<UserInfo> users) {
		this.users = users;
	}
}
