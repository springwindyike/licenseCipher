package com.ejiahe.sdk.jsonrpcbean;

import java.util.List;

/**
 * Created by liaofei on 2018/1/26.
 */
public class DepartmentAndUsersInfo {
    private String departmentId;
    private String departmentName;
    private String parentId;
    private int userCount;
    private List<UserSimpleInfo> users;

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public List<UserSimpleInfo> getUsers() {
        return users;
    }

    public void setUsers(List<UserSimpleInfo> users) {
        this.users = users;
    }
}
