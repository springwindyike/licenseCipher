package com.ejiahe.sdk.jsonrpcbean;

import java.util.List;

/**
 * Created by liaofei on 2018/1/11.
 */
public class ItemsInfoOfTenement<T> {
    private Integer count;

    private List<T> tenements;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<T> getTenements() {
        return tenements;
    }

    public void setTenements(List<T> tenements) {
        this.tenements = tenements;
    }
}
