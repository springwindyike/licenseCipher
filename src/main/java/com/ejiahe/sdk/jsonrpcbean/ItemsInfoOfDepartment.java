package com.ejiahe.sdk.jsonrpcbean;

import java.util.List;

/**
 * Created by liaofei on 2018/1/11.
 */
public class ItemsInfoOfDepartment<T> {

    private List<T> departments;

    public List<T> getDepartments() {
        return departments;
    }

    public void setDepartments(List<T> departments) {
        this.departments = departments;
    }
}
