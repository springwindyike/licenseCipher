package com.ejiahe.jeservice.jsonrpc.packet;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by mjf on 2018/1/5.
 */
public abstract class JSONRPCPacket {


    public abstract JSONObject toJSONObject();

    public String toJSONString(){
        return toJSONObject().toJSONString();
    }

    @Override
    public String toString() {
        return toJSONString();
    }
}
