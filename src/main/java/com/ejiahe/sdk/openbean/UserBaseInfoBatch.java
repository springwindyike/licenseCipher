package com.ejiahe.sdk.openbean;

import com.ejiahe.jeservice.util.CodeResult;

import java.util.List;

/**
 * 用户基本信息;
 * Created by mjf on 2017/4/14.
 */
public class UserBaseInfoBatch extends CodeResult {

    private List<UserBaseInfo> items;

    public List<UserBaseInfo> getItems() {
        return items;
    }

    public void setItems(List<UserBaseInfo> items) {
        this.items = items;
    }
}
