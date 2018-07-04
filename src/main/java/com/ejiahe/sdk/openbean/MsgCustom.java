package com.ejiahe.sdk.openbean;

/**
 * Created by Administrator on 2017/3/16.
 */
public class MsgCustom implements Msg{

    private String type;
    private String data;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String getMsg_type() {
        return Msg.TYPE_CUSTOM;
    }
}
