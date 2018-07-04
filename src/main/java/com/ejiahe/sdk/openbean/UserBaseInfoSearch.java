package com.ejiahe.sdk.openbean;

import com.ejiahe.jeservice.util.CodeResult;

import java.util.List;

/**
 * 用户基本信息;
 * Created by mjf on 2017/4/14.
 */
public class UserBaseInfoSearch extends CodeResult {

    private List<UserBaseInfo> items;

    private int start_index;
    private int max_items;
    private int total_items;
    private int current_items;

    public List<UserBaseInfo> getItems() {
        return items;
    }

    public void setItems(List<UserBaseInfo> items) {
        this.items = items;
    }

    public int getCurrent_items() {
        return current_items;
    }

    public void setCurrent_items(int current_items) {
        this.current_items = current_items;
    }

    public int getStart_index() {
        return start_index;
    }

    public void setStart_index(int start_index) {
        this.start_index = start_index;
    }

    public int getMax_items() {
        return max_items;
    }

    public void setMax_items(int max_items) {
        this.max_items = max_items;
    }

    public int getTotal_items() {
        return total_items;
    }

    public void setTotal_items(int total_items) {
        this.total_items = total_items;
    }
}
