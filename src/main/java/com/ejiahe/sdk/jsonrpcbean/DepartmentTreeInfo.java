package com.ejiahe.sdk.jsonrpcbean;

import java.util.List;

/**
 * 部门树结构
 * Created by liaofei on 2018/1/24.
 */
public class DepartmentTreeInfo {
    private String id;
    private String name;
    private int userCount;
    private String icon;
    private boolean open;
    private List<DepartmentTreeInfo> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public List<DepartmentTreeInfo> getChildren() {
        return children;
    }

    public void setChildren(List<DepartmentTreeInfo> children) {
        this.children = children;
    }
}
