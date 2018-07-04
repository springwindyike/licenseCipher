package com.ejiahe.jeservice.jsonrpc.packet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by mjf on 2018/1/5.
 */
public class JSONRPCRequest extends JSONRPCPacket{

    private static final AtomicLong idNum = new AtomicLong(0);

    private Object id;

    private String method;

    private JSONObject params;

    public static JSONRPCRequest parse(final String val) throws JSONRPCParseException {
        try{
            JSONRPCRequest request =  JSON.toJavaObject(JSON.parseObject(val), JSONRPCRequest.class);

            if(request.getId() == null){
                throw new IllegalArgumentException("the id can not found");
            }

            return request;

        }catch (Exception e){
            throw new JSONRPCParseException("Invalid JSON: " + e.getMessage() , JSONRPCParseException.JSON, val);
        }
    }

    public JSONRPCRequest(String method){
        this(method,null,null);
    }


    public JSONRPCRequest(String method, JSONObject params){
        this(method,params,null);
    }

    public JSONRPCRequest(String method, JSONObject params, Object id){
        this.method = method;
        this.params = params;

        if(id != null){
            this.id = id;
        }else{
            this.id = idNum.incrementAndGet();
        }
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
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

    public String getString(String key ){
        return getParams().getString(key);
    }

    public Integer getInteger(String key){
        return getParams().getInteger(key);
    }

    public Boolean getBoolean(String key){
        return getParams().getBoolean(key);
    }

    public Long getLong(String key){
        return getParams().getLong(key);
    }

    public <T> T getParamsAsObject(Class<T> tempClass){
        if(this.params == null){
            return null;
        }else{
            return JSON.toJavaObject(this.params,tempClass);
        }
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

        req.put("id", id);

        req.put("jsonrpc", "2.0");

        return req;
    }
}
