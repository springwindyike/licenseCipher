package com.ejiahe.jeservice.jsonrpc.packet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by mjf on 2018/1/5.
 */
public class JSONRPCNotification extends JSONRPCPacket{

    private String method;

    private JSONObject params;

    public static JSONRPCNotification parse(final String val) throws JSONRPCParseException {
        try{
            JSONRPCNotification request =  JSON.toJavaObject(JSON.parseObject(val), JSONRPCNotification.class);
            return request;
        }catch (Exception e){
            throw new JSONRPCParseException("Invalid JSON: " + e.getMessage() , JSONRPCParseException.JSON, val);
        }
    }

    public JSONRPCNotification(String method){
        this(method,null);
    }

    public JSONRPCNotification(String method, JSONObject params){
        this.method = method;
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject req = new JSONObject();

        req.put("method", method);

        // the params can be omitted if empty
        if (params != null)
            req.put("params", params);

        req.put("jsonrpc", "2.0");

        return req;
    }
}
